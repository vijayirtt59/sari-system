package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private BusinessRole businessRole;

    @Enumerated(EnumType.STRING)
    private SystemRole systemRole;

    private boolean enabled;

    public String getFullName() {

        return firstName + " " + lastName;
    }



}
