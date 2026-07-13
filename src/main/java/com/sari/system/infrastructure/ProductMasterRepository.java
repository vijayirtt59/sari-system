package com.sari.system.infrastructure;

import com.sari.system.domain.ProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductMasterRepository
        extends JpaRepository<ProductMaster, Long> {

    Optional<ProductMaster> findByNameIgnoreCase(
            String name
    );
}

