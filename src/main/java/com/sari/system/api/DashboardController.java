package com.sari.system.api;


import com.sari.system.application.DashboardService;
import com.sari.system.domain.BusinessRole;
import com.sari.system.domain.Pro;
import com.sari.system.domain.User;
import com.sari.system.dto.DashboardOverview;
import com.sari.system.dto.DocumentSummary;
import com.sari.system.dto.MyResponsibilities;
import com.sari.system.dto.PendingItem;
import com.sari.system.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    @GetMapping("/pending")
    public List<PendingItem> pending() {
        return dashboardService.getPendingItems();
    }

    @GetMapping("/overview")
    public DashboardOverview overview() {
        return dashboardService.getOverview();
    }

    @GetMapping("/recent")
    public List<DocumentSummary> recent() {
        return dashboardService.getRecent();
    }

    @GetMapping("/my-responsibilities/{role}")
    public MyResponsibilities getResponsibilities(
            @PathVariable String role
    ) {

        BusinessRole businessRole =
                Arrays.stream(BusinessRole.values())
                        .filter(r ->
                                r.name().equalsIgnoreCase(role) ||
                                        r.getLabel().equalsIgnoreCase(role))
                        .findFirst()
                        .orElseThrow();

        return dashboardService.getResponsibilities(
                businessRole
        );
    }


}
