package com.sari.system.infrastructure;

import com.sari.system.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByTitleContainingIgnoreCase(String keyword);
    Optional<Section> findByCode(String code);

}
