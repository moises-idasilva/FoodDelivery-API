package com.moises.foodapp.api.controller;

import com.moises.foodapp.domain.exception.EntidadeEmUsoException;
import com.moises.foodapp.domain.exception.EntidadeNaoEncontradaException;
import com.moises.foodapp.domain.model.Cidade;
import com.moises.foodapp.domain.repository.CidadeRepository;
import com.moises.foodapp.domain.service.CadastroCidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {

        Optional<Cidade> cidade = cidadeRepository.findById(id);

        if (cidade.isPresent()) {
            return ResponseEntity.ok(cidade.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {

        try {
            cidade = cadastroCidadeService.salvar(cidade);

            return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {

        Cidade cidadeAtual = cidadeRepository.findById(id).orElse(null);

        if (cidade != null) {
            BeanUtils.copyProperties(cidade, cidadeAtual, "id");

            cidadeAtual = cadastroCidadeService.salvar(cidadeAtual);
            return ResponseEntity.ok(cidadeAtual);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {

        try {
            cadastroCidadeService.excluir(id);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {
            System.out.println("=> EXCEPTION: " + e);
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            System.out.println("=> EXCEPTION: " + e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }


}
