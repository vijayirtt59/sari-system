package com.sari.system.api;

import com.sari.system.audit.AuditLog;
import com.sari.system.infrastructure.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public List<AuditLog> getRecentAudit(){
        return auditLogRepository.findTop10ByOrderByCreatedAtDesc();
    }
}
