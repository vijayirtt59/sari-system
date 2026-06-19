package com.sari.system.api;


import com.sari.system.domain.Pro;
import com.sari.system.infrastructure.ProRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin
public class DashboardController {


    private final ProRepository proRepository;

    @GetMapping("/stats")
    public Map<String, Object> stats() {

        Map<String, Object> map =
                new HashMap<>();

        long total =
                proRepository.count();

        long approved =
                proRepository.countByStatus("APPROVED");

        long reviewed =
                proRepository.countByStatus("REVIEWED");

        long prepared =
                proRepository.countByStatus("PREPARED");

        long draft =
                proRepository.countByStatus("DRAFT");

        map.put("total", total);

        map.put("approved", approved);

        map.put("reviewed", reviewed);

        map.put("prepared", prepared);

        map.put("draft", draft);

        return map;
    }

    @GetMapping("/pending")
    public List<Map<String, Object>> pending() {

        List<Pro> pros =
                proRepository.findByStatusIn(
                        List.of("PREPARED", "REVIEWED")
                );

        return pros.stream().map(p -> {

            Map<String, Object> map =
                    new HashMap<>();

            map.put("code", p.getCode());
            map.put("title", p.getTitle());
            map.put("status", p.getStatus());

            return map;

        }).toList();
    }


}
