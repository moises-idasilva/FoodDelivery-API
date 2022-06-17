package com.moises.foodapp.api.controller;

import com.moises.foodapp.api.model.CozinhasXmlWrapper;
import com.moises.foodapp.domain.exception.EntidadeEmUsoException;
import com.moises.foodapp.domain.exception.EntidadeNaoEncontradaException;
import com.moises.foodapp.domain.model.Cozinha;
import com.moises.foodapp.domain.repository.CozinhaRepository;
import com.moises.foodapp.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    private Cozinha cozinha;

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;


    @GetMapping(produces = "application/json")
    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    // Apenas para referência de como retornar um XML
    @GetMapping(produces = "application/xml")
    public CozinhasXmlWrapper listarXML() {
        return new CozinhasXmlWrapper(cozinhaRepository.findAll());
    }

    //ResponseEntity permite customizar a resposta HTTP
    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {

        Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);

        /**
         Abaixo as formas corretas de retornar o ResponseEntity para a Busca
         */

        //return cozinha.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        if (cozinha.isPresent()) {
            return ResponseEntity.ok(cozinha.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/busca-por-nome")
    public ResponseEntity<List<Cozinha>> cozinhasPorNome(String nome) {
        List<Cozinha> cozinha = cozinhaRepository.findByNomeIsContainingIgnoreCase(nome);

        if (!cozinha.isEmpty()) {
            return ResponseEntity.ok(cozinha);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody Cozinha cozinha) {
        return cadastroCozinhaService.salvar(cozinha);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cozinha> atualizar(@PathVariable Long id, @RequestBody Cozinha cozinha) {

        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(id);

        //Utilizar o BeanUtils para copiar as propriedades de cozinha para cozinhaAtual.
        if (cozinhaAtual.isPresent()) {
            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");

            Cozinha cozinhaSalva = cadastroCozinhaService.salvar(cozinhaAtual.get());
            return ResponseEntity.ok(cozinhaSalva);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    //    @DeleteMapping("/{id}")
//    public ResponseEntity<Cozinha> remover(@PathVariable Long id) {
//
//        try {
//            cadastroCozinhaService.excluir(id);
//            return ResponseEntity.noContent().build();
//
//        } catch (EntidadeNaoEncontradaException e) {
//            System.out.println("=> EXCEPTION: " + e);
//            return ResponseEntity.notFound().build();
//
//        } catch (EntidadeEmUsoException e) {
//            System.out.println("=> EXCEPTION: " + e);
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//
//    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cadastroCozinhaService.excluir(id);

    }

}
