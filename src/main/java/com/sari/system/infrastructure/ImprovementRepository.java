package com.sari.system.infrastructure;

import com.sari.system.domain.Improvement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImprovementRepository extends JpaRepository<Improvement, Long> {
}
