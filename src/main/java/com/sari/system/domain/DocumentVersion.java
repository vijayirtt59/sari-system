package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class DocumentVersion {

    @Id
    @GeneratedValue
    private Long id;

    private String documentCode;
    private String title;
    private String name;

    private int version;

    private String changes;


    // ✅ CONTENT SECTIONS (RICH HTML)
    @Lob
    private String objetivo;

    @Lob
    private String alcance;

    @Lob
    private String procedimiento;

    @Lob
    private String registros;
    @ManyToMany
    private List<Section> sections;

    DocumentStatus status;



    @Lob
    private String content;

    private String preparedBy;
    private String reviewedBy;
    private String approvedBy;

    private LocalDate preparedDate;
    private LocalDate reviewedDate;
    private LocalDate approvedDate;

    private String fileName;

    private LocalDate createdAt;

}
