package com.neorios.neorios.repository;

import com.neorios.neorios.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuentaOrigenIdOrCuentaDestinoId(Long cuentaOrigenId, Long cuentaDestinoId);
}