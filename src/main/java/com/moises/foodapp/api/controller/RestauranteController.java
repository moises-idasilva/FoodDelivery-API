package com.moises.foodapp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moises.foodapp.domain.exception.EntidadeEmUsoException;
import com.moises.foodapp.domain.exception.EntidadeNaoEncontradaException;
import com.moises.foodapp.domain.model.Restaurante;
import com.moises.foodapp.domain.repository.RestauranteRepository;
import com.moises.foodapp.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @GetMapping
    public List<Restaurante> listar() {

        return restauranteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long id) {

        Optional<Restaurante> restaurante = restauranteRepository.findById(id);

        if (restaurante.isPresent()) {
            return ResponseEntity.ok(restaurante.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/por-taxa-frete")
    public List<Restaurante> restaurantesPorTaxaFrete(
            BigDecimal taxaInicial, BigDecimal taxaFinal) {
        return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
    }

    @GetMapping("/por-nome")
    public List<Restaurante> restaurantesPorNome(String nome) {
        return restauranteRepository.findByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("/por-nome-e-cozinhaid")
    public List<Restaurante> restaurantesPorNomeECozinhaId(String nome, Long cozinhaId) {
        return restauranteRepository.listarPorNomeECozinhaId(nome, cozinhaId);
        //return restauranteRepository.findByNomeContainingAndCozinhaId(nome, cozinhaId);
    }

    @GetMapping("/por-nome-e-ou-frete")
    public List<Restaurante> restaurantesPorNomeFrete(String nome,
                                                      BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
    }

    @PostMapping
    public Restaurante adicionar(@RequestBody Restaurante restaurante) {

        return cadastroRestauranteService.salvar(restaurante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {

       Restaurante restauranteAtual = restauranteRepository.findById(id).orElse(null);

        if (restauranteAtual != null) {
            BeanUtils.copyProperties(restaurante, restauranteAtual,
                    "id", "formasPagamento", "endereco", "dataCadastro", "produtos");

            restauranteAtual = cadastroRestauranteService.salvar(restauranteAtual);
            return ResponseEntity.ok(restauranteAtual);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // Para atualização parcial
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {

        Restaurante restauranteAtual = restauranteRepository.findById(id).orElse(null);

        if (restauranteAtual == null) {
            return ResponseEntity.notFound().build();
        }

        //chamar o método merge() que ira mesclar as informações da entidade chamada com a entidade temporária
        merge(campos, restauranteAtual);

        return atualizar(id, restauranteAtual);
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {

        // objectMapper para fazer a conversão automática dos valores
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {

            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            field.setAccessible(true); //para acessar uma variável privada

            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

            System.out.println(nomePropriedade + " = " + novoValor);

            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {

        try {
            cadastroRestauranteService.excluir(id);
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
