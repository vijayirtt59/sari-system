package com.sari.system.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ProPdfVersion {

    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private int version;
    private String fileName;
    private String createdBy;
    private String createdDate;

}
