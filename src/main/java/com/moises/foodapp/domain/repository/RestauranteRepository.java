package com.moises.foodapp.domain.repository;

import com.moises.foodapp.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long>, RestauranteRepositoryQueries {

    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

    //@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
    // A procura abaixo está utilizando XML para Query (ver: Resources > META-INF > orm.xml)
    List<Restaurante> listarPorNomeECozinhaId(String nome, @Param("id") Long cozinha);
    //List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);

    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Resolvendo o Problema do N+1 com fetch join na JPQL
    //  @Query("from Restaurante r join fetch r.cozinha left join fetch r.formaPagamentos")
    @Query("from Restaurante r join fetch r.cozinha")
    List<Restaurante> findAll();


}
