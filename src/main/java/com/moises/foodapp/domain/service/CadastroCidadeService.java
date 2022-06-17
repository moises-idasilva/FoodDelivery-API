package com.moises.foodapp.domain.service;

import com.moises.foodapp.domain.exception.EntidadeEmUsoException;
import com.moises.foodapp.domain.exception.EntidadeNaoEncontradaException;
import com.moises.foodapp.domain.model.Cidade;
import com.moises.foodapp.domain.model.Estado;
import com.moises.foodapp.domain.repository.CidadeRepository;
import com.moises.foodapp.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroCidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade salvar(Cidade cidade) {

        Long estadoId = cidade.getEstado().getId();
        Estado estado = estadoRepository.findById(estadoId).orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format("Não existe cadastro de estado com código %d", estadoId)));

        cidade.setEstado(estado);

        return cidadeRepository.save(cidade);
    }

    public void excluir(Long cidadeId) {

        try {
            cidadeRepository.deleteById(cidadeId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format(
                            "Não existe um cadastro de Cidade com ID:  %d", cidadeId).toUpperCase());
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(
                            "Cidade de código ID %d não pode ser removida, pois está em uso", cidadeId).toUpperCase());
        }

    }

}
