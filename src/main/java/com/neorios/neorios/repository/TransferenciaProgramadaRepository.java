package com.neorios.neorios.repository;

import com.neorios.neorios.model.TransferenciaProgramada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferenciaProgramadaRepository extends JpaRepository<TransferenciaProgramada, Long> {
    List<TransferenciaProgramada> findByCuentaOrigenId(Long cuentaOrigenId);
    List<TransferenciaProgramada> findByActivaTrue();
}