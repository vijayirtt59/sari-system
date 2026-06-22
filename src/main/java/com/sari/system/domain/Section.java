package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Section {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String code;
    private String title;
    private String name;
    private LocalDate documentDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int orderIndex;

    private LocalDate createdAt;
    private String imageUrl;

}
