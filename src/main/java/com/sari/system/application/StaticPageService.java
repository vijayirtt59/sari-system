package com.sari.system.application;

import com.sari.system.domain.StaticPage;
import com.sari.system.infrastructure.StaticPageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StaticPageService {


    private final StaticPageRepository repo;

    public StaticPageService(StaticPageRepository repo) {
        this.repo = repo;
    }

    public StaticPage save(String code, String content) {

        StaticPage page = repo.findById(code).orElse(new StaticPage());

        page.setCode(code);
        page.setContent(content);
        page.setUpdatedAt(LocalDateTime.now());

        return repo.save(page);
    }

    public StaticPage get(String code) {
        return repo.findById(code).orElse(null);
    }

}
