package com.minhaCarteira.crud.domain.gasto.repository;

import com.minhaCarteira.crud.domain.gasto.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GastoRepository extends JpaRepository<Gasto, Integer>, JpaSpecificationExecutor<Gasto> {

}
