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
import java.util.Set;


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

                .professionalTitle(req.getProfessionalTitle())

                .password(
                        passwordEncoder.encode(
                                req.getPassword()
                        )
                )

                // ✅ default role
                .systemRoles(
                        Set.of(SystemRole.VIEWER)
                )

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

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled.");
        }

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



}
