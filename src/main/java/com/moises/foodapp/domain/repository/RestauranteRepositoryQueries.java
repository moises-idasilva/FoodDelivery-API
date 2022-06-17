package com.moises.foodapp.domain.repository;

import com.moises.foodapp.domain.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepositoryQueries {

    // Abaixo exemplo de consulta dinâmica implementada em: infrastructure > repository > RestauranteRepositoryImpl
    // Não esquecer de adicionar essa classe na assinatura do repositório original para que o método possa ser usado no controller.
    List<Restaurante> find(String nome,
                           BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

}
