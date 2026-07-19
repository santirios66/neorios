package com.neorios.neorios.repository;

import com.neorios.neorios.model.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    List<LogAuditoria> findAllByOrderByFechaDesc();
}