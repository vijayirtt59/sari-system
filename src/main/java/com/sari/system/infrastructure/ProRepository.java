package com.sari.system.infrastructure;

import com.sari.system.domain.Pro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProRepository extends JpaRepository<Pro, Long> {
    Optional<Pro> findByCode(String code);
    Optional<Pro> findTopByCodeOrderByVersionDesc(String code);
    List<Pro> findByCodeOrderByVersionDesc(String code);
    long countByStatus(String status);
    List<Pro> findByStatusIn(List<String> statuses);

}
