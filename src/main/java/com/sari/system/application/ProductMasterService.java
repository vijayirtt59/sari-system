package com.sari.system.application;

import com.sari.system.domain.ProductMaster;
import com.sari.system.infrastructure.ProductMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMasterService {

    private final ProductMasterRepository repo;

    public List<ProductMaster> findAll() {

        return repo.findAll(
                Sort.by("name")
        );
    }

    public ProductMaster save(
            ProductMaster product
    ) {

        return repo.save(product);
    }
}
