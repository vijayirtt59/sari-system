package com.sari.system.config;

import com.sari.system.domain.BusinessRole;
import com.sari.system.domain.SystemRole;
import com.sari.system.domain.User;
import com.sari.system.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@sari.com").isEmpty()) {

            userRepository.save(
                    User.builder()
                            .professionalTitle("Ing.")
                            .firstName("System")
                            .lastName("Admin")
                            .email("admin@sari.com")
                            .password(passwordEncoder.encode("admin123"))
                            .businessRole(
                                    BusinessRole.COORDINADOR_SARI
                            )
                            .systemRoles(
                                    Set.of(
                                            SystemRole.ADMIN,
                                            SystemRole.PREPARER,
                                            SystemRole.REVIEWER,
                                            SystemRole.APPROVER
                                    )
                            )
                            .enabled(true)
                            .build()
            );
        }

        createUser(
                "Lic.",
                "Maria",
                "Ventas",
                "ventas@sari.com",
                "test123",
                BusinessRole.AGENTES_VENTAS,
                Set.of(SystemRole.PREPARER)
        );

        createUser(
                "Ing.",
                "Carlos",
                "Compras",
                "compras@sari.com",
                "test123",
                BusinessRole.RESPONSABLE_VENTAS_COMPRAS,
                Set.of(SystemRole.PREPARER)
        );

        createUser(
                "Lic.",
                "Angelica",
                "Silva",
                "angelica@sari.com",
                "test123",
                BusinessRole.COORDINADOR_SARI,
                Set.of(SystemRole.REVIEWER)
        );

        createUser(
                "Ing.",
                "Jorge",
                "Logistica",
                "logistica@sari.com",
                "test123",
                BusinessRole.LOGISTICA,
                Set.of(
                        SystemRole.PREPARER,
                        SystemRole.REVIEWER
                )
        );

        createUser(
                "Ing.",
                "Pedro",
                "Produccion",
                "produccion@sari.com",
                "test123",
                BusinessRole.PRODUCCION,
                Set.of(SystemRole.PREPARER)
        );

        createUser(
                "Lic.",
                "Ana",
                "Direccion",
                "direccion@sari.com",
                "test123",
                BusinessRole.DIRECCION_GENERAL,
                Set.of(SystemRole.APPROVER)
        );

        createUser(
                "Ing.",
                "Laura",
                "Sistema",
                "sistema@sari.com",
                "test123",
                BusinessRole.COORDINADOR_SISTEMA_SARI,
                Set.of(
                        SystemRole.PREPARER,
                        SystemRole.REVIEWER,
                        SystemRole.APPROVER
                )
        );

        createUser(
                null,
                "Viewer",
                "Only",
                "viewer@sari.com",
                "test123",
                BusinessRole.AGENTES_VENTAS,
                Set.of(SystemRole.VIEWER)
        );
    }

    private void createUser(
            String professionalTitle,
            String firstName,
            String lastName,
            String email,
            String password,
            BusinessRole businessRole,
            Set<SystemRole> roles
    ) {

        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        userRepository.save(
                User.builder()
                        .professionalTitle(professionalTitle)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(
                                passwordEncoder.encode(password)
                        )
                        .businessRole(businessRole)
                        .systemRoles(roles)
                        .enabled(true)
                        .build()
        );
    }

}
