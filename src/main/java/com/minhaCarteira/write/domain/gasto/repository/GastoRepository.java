package com.minhaCarteira.write.domain.gasto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.minhaCarteira.write.domain.gasto.Gasto;

public interface GastoRepository extends JpaRepository<Gasto, Integer>, JpaSpecificationExecutor<Gasto> {

}
