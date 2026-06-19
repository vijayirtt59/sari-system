package com.sari.system.infrastructure;

import com.sari.system.domain.Docs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocRepository extends JpaRepository<Docs, Long> {

    Optional<Docs> findByCode(String code);
    List<Docs> findAllByOrderByCodeAsc();

}
