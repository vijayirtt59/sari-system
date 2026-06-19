package com.sari.system.infrastructure;

import com.sari.system.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findTop10ByOrderByCreatedAtDesc();

}
