package com.sari.system.api;

import com.sari.system.application.ProductMasterService;
import com.sari.system.domain.ProductMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductMasterController {

    private final ProductMasterService service;

    @GetMapping
    public List<ProductMaster> all() {

        return service.findAll();
    }

    @PostMapping
    public ProductMaster create(
            @RequestBody ProductMaster product
    ) {

        return service.save(product);
    }
}
