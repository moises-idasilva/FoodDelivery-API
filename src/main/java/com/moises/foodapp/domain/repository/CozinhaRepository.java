package com.moises.foodapp.domain.repository;

import com.moises.foodapp.domain.model.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

    List<Cozinha> findByNomeIsContainingIgnoreCase(String nome);


}
