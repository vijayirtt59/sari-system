package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Pro {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;
    private String title;
    private String name;
    private LocalDate documentDate;

    @Column(columnDefinition = "TEXT")
    private String objetivo;

    @Column(columnDefinition = "TEXT")
    private String alcance;

    @Column(columnDefinition = "TEXT")
    private String procedimiento;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "pro_id")
    private List<RegistroItem> registros;

    private int version = 1;

    private String status = "DRAFT";

    private String preparedBy;
    private String reviewedBy;
    private String approvedBy;

    private LocalDate preparedDate;
    private LocalDate reviewedDate;
    private LocalDate approvedDate;

    private String lastModifiedBy;
    private String lastModifiedDate;


}
