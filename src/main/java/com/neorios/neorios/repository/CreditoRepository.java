package com.neorios.neorios.repository;

import com.neorios.neorios.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CreditoRepository extends JpaRepository<Credito, Long> {
    List<Credito> findByUsuarioId(Long usuarioId);
    List<Credito> findByEstado(String estado);
}