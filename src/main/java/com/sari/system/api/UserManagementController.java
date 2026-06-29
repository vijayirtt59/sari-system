package com.sari.system.api;

import com.sari.system.domain.BusinessRole;
import com.sari.system.domain.SystemRole;
import com.sari.system.domain.User;
import com.sari.system.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> users() {

        List<User> users = userRepository.findAll();
        return users;
    }

    @PutMapping("/{id}/business-role")
    public User updateBusinessRole(
            @PathVariable Long id,
            @RequestParam BusinessRole businessRole
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setBusinessRole(businessRole);

        return userRepository.save(user);
    }

    @PutMapping("/{id}/roles")
    public User updateRoles(
            @PathVariable Long id,
            @RequestBody Set<SystemRole> roles
    ) {

        User user = userRepository
                .findById(id)
                .orElseThrow();

        user.setSystemRoles(roles);

        return userRepository.save(user);
    }

    @PutMapping("/{id}/enabled")
    public User updateEnabled(
            @PathVariable Long id,
            @RequestParam boolean enabled
    ) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        user.setEnabled(enabled);

        return userRepository.save(user);
    }
}
