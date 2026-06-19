package com.sari.system.application;

import com.sari.system.domain.Improvement;
import com.sari.system.infrastructure.ImprovementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ImprovementService {


    private final ImprovementRepository repo;

    public ImprovementService(ImprovementRepository repo) {
        this.repo = repo;
    }

    public Improvement create(Improvement i) {
        i.setStatus("OPEN");
        i.setCreatedDate(LocalDate.now());
        return repo.save(i);
    }

    public List<Improvement> getAll() {
        return repo.findAll();
    }

    public Improvement updateStatus(Long id, String status) {
        Improvement i = repo.findById(id).orElseThrow();
        i.setStatus(status);
        return repo.save(i);
    }


}
