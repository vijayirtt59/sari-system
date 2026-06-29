package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity

@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String professionalTitle;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private BusinessRole businessRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_system_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<SystemRole> systemRoles =
            new HashSet<>();

    private boolean enabled;

    public String getFullName() {

        return firstName + " " + lastName;
    }

    public String getWorkflowName(){

        if (getProfessionalTitle() == null ||
                getProfessionalTitle().isBlank()) {

            return getFullName();
        }

        return getProfessionalTitle() + " " + getFullName();
    }



}
