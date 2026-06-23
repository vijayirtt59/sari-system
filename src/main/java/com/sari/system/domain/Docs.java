package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(columnDefinition = "TEXT")
    private String content;

    private int version = 0;

    private String status = "DRAFT";

    private String preparedBy;
    private String reviewedBy;
    private String approvedBy;

    private LocalDate preparedDate;
    private LocalDate reviewedDate;
    private LocalDate approvedDate;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "doc_id")
    private List<DocumentChange> changes = new ArrayList<>();

    @Transient
    private String changeDescription;

}
