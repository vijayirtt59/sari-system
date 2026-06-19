package com.sari.system.infrastructure;

import com.sari.system.domain.ProPdfVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProPdfVersionRepository extends JpaRepository<ProPdfVersion, Long> {


    @Query("SELECT MAX(p.version) FROM ProPdfVersion p WHERE p.code = :code")
    Optional<Integer> findMaxVersion(@Param("code") String code);

}
