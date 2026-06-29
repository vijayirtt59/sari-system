package com.sari.system.application;

import com.sari.system.domain.Docs;

import java.util.List;

public interface DocService {

    Docs create(Docs doc);

    Docs update(Long id, Docs doc);

    void delete(Long id);

    List<Docs> getAll();

    Docs getByCode(String code);

    byte[] generateDocsPdf(String code) throws Exception;

    Docs action(Long id, String action, Long userId);
    
}
