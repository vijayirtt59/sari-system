package com.sari.system.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DocumentSummary {

    private String code;

    private String title;

    private String status;

    private String type;

    private LocalDate date;
}