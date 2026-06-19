package com.sari.system.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class StaticPage {

    @Id
    private String code; // DOC-01, DOC-02

    @Lob
    private String content; // HTML content

    private LocalDateTime updatedAt;

}
