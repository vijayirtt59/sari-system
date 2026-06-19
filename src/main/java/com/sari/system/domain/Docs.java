package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Docs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String title;

    private String name;

    private LocalDate date;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

}
