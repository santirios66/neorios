package com.neorios.neorios.repository;

import com.neorios.neorios.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    List<Tarjeta> findByCuentaId(Long cuentaId);
    Optional<Tarjeta> findByNumeroTarjeta(String numeroTarjeta);
}