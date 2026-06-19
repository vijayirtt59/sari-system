package com.sari.system.infrastructure;

import com.sari.system.domain.FormTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormTemplateRepository extends JpaRepository<FormTemplate, Long> {

    Optional<FormTemplate> findByCode(String code);

}
