package com.neorios.neorios.repository;

import com.neorios.neorios.model.MetaAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MetaAhorroRepository extends JpaRepository<MetaAhorro, Long> {
    List<MetaAhorro> findByUsuarioId(Long usuarioId);
}