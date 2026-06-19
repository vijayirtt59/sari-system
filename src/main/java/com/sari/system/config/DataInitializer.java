package com.sari.system.config;

import com.sari.system.domain.BusinessRole;
import com.sari.system.domain.SystemRole;
import com.sari.system.domain.User;
import com.sari.system.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ✅ create admin only if DB empty

        if (userRepository.count() == 0) {

            User admin = User.builder()

                    .firstName("System")

                    .lastName("Admin")

                    .email("admin@sari.com")

                    .password(
                            passwordEncoder.encode("admin123")
                    )

                    .businessRole(BusinessRole.COORDINADOR_SARI)

                    .systemRole(SystemRole.ADMIN)

                    .enabled(true)

                    .build();

            userRepository.save(admin);

            System.out.println(
                    "✅ Default admin created"
            );
        }
    }

}
