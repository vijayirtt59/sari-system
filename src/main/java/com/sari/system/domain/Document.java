package com.sari.system.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Document {

    @Id
    private String code; // PRO-01, DOC-03

    private String title;
    private String name;
    private String type; // DOC, PRO, SECCION


    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private int version;

    private LocalDate createdDate;

    private String fileName; // ✅ stores uploaded file


}
