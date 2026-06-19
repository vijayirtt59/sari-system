package com.sari.system.infrastructure;

import com.sari.system.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, String> {
}
