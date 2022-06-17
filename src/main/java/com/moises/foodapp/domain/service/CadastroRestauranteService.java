package com.moises.foodapp.domain.service;

import com.moises.foodapp.domain.exception.EntidadeEmUsoException;
import com.moises.foodapp.domain.exception.EntidadeNaoEncontradaException;
import com.moises.foodapp.domain.model.Cozinha;
import com.moises.foodapp.domain.model.Restaurante;
import com.moises.foodapp.domain.repository.CozinhaRepository;
import com.moises.foodapp.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante) {

        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.findById(cozinhaId).orElseThrow(() -> new EntidadeNaoEncontradaException(
                    String.format("Não existe cadastro de cozinha com o código %d", cozinhaId)));

        restaurante.setCozinha(cozinha);

        return restauranteRepository.save(restaurante);
    }

    public void excluir(Long restauranteId) {

        try {
            restauranteRepository.deleteById(restauranteId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de restaurante com código %d.", restauranteId).toUpperCase());
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Não existe um cadastro de restaurante com código %d.", restauranteId).toUpperCase());
        }

    }

}
