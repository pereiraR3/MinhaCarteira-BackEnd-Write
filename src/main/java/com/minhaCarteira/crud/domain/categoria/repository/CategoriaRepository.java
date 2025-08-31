package com.minhaCarteira.crud.domain.categoria.repository;

import com.minhaCarteira.crud.domain.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
