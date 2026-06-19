package com.sari.system.infrastructure;

import com.sari.system.domain.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionRepository extends JpaRepository<Distribution, Long> {
}
