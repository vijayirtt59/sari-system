package com.sari.system.infrastructure;

import com.sari.system.domain.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    DocumentVersion findTopByDocumentCodeOrderByVersionDesc(String code);


}
