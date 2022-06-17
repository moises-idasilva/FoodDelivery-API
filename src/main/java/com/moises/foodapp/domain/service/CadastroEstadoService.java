package com.moises.foodapp.domain.service;

import com.moises.foodapp.domain.exception.EntidadeEmUsoException;
import com.moises.foodapp.domain.exception.EntidadeNaoEncontradaException;
import com.moises.foodapp.domain.model.Estado;
import com.moises.foodapp.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado) {

        return estadoRepository.save(estado);

    }

    public void excluir(Long id) {

        try {
            estadoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de Estado com ID:  %d", id).toUpperCase());
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(
                            "Estado de código ID %d não pode ser removida, pois está em uso", id).toUpperCase());
        }

    }

}
