package com.moises.foodapp.api.controller;

import com.moises.foodapp.domain.exception.EntidadeEmUsoException;
import com.moises.foodapp.domain.exception.EntidadeNaoEncontradaException;
import com.moises.foodapp.domain.model.Estado;
import com.moises.foodapp.domain.repository.EstadoRepository;
import com.moises.foodapp.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscar(@PathVariable Long id) {

        Optional<Estado> estado = estadoRepository.findById(id);

        if (estado.isPresent()) {
            return ResponseEntity.ok(estado.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public Estado adicionar(@RequestBody Estado estado) {

        return cadastroEstadoService.salvar(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Estado estado) {

        Estado estadoAtual = estadoRepository.findById(id).orElse(null);

        if (estadoAtual != null) {
            BeanUtils.copyProperties(estado, estadoAtual, "id");

            estadoAtual = cadastroEstadoService.salvar(estadoAtual);
            return ResponseEntity.ok(estadoAtual);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {

        try {
            cadastroEstadoService.excluir(id);
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
