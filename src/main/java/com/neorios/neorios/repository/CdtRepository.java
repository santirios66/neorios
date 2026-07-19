package com.neorios.neorios.repository;

import com.neorios.neorios.model.Cdt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CdtRepository extends JpaRepository<Cdt, Long> {
    List<Cdt> findByUsuarioId(Long usuarioId);
}