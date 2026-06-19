package com.sari.system.api;


import com.sari.system.domain.BusinessRole;
import com.sari.system.domain.SystemRole;
import com.sari.system.domain.User;
import com.sari.system.dto.LoginRequest;
import com.sari.system.dto.RegisterRequest;
import com.sari.system.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public User register(
            @RequestBody RegisterRequest req
    ) {

        if (userRepository
                .findByEmail(req.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        User user = User.builder()

                .firstName(req.getFirstName())

                .lastName(req.getLastName())

                .email(req.getEmail())

                .password(
                        passwordEncoder.encode(
                                req.getPassword()
                        )
                )

                // ✅ default role
                .systemRole(SystemRole.VIEWER)

                .enabled(true)

                .build();

        return userRepository.save(user);
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public User login(
            @RequestBody LoginRequest req
    ) {

        User user = userRepository
                .findByEmail(req.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email"
                        )
                );

        boolean ok =
                passwordEncoder.matches(
                        req.getPassword(),
                        user.getPassword()
                );

        if (!ok) {

            throw new RuntimeException(
                    "Invalid password"
            );
        }

        return user;
    }

    @PutMapping("/{id}/role")
    public User updateRole(
            @PathVariable Long id,
            @RequestParam SystemRole role
    ) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        user.setSystemRole(role);

        return userRepository.save(user);
    }

    @GetMapping("/users")
    public List<User> users() {

        List<User> users = userRepository.findAll();
        return users;
    }

    @PutMapping("/users/{id}/business-role")
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

}
