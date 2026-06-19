package com.sari.system.api;


import com.sari.system.application.ImprovementService;
import com.sari.system.domain.Improvement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/improvements")
public class ImprovementController {

    private final ImprovementService service;

    public ImprovementController(ImprovementService service) {
        this.service = service;
    }

    @PostMapping
    public Improvement create(@RequestBody Improvement i) {
        return service.create(i);
    }

    @GetMapping
    public List<Improvement> list() {
        return service.getAll();
    }

    @PostMapping("/{id}/status")
    public Improvement update(@PathVariable Long id,
                              @RequestParam String status) {
        return service.updateStatus(id, status);
    }

}
