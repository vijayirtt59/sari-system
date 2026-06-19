package com.sari.system.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Improvement {

    @Id
    @GeneratedValue
    private Long id;

    private String description;

    private String area;

    private String reportedBy;

    private String status; // CREATED, APPROVED, CLOSED

    private LocalDate createdDate;

}
