package com.sari.system.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Distribution {

    @Id
    @GeneratedValue
    private Long id;

    private String documentCode;

    private String userName;

    private LocalDate date;

    private boolean acknowledged;

}
