package com.sari.system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProChangeRequest {

    private int version;

    private String description;

    private LocalDate changeDate;

}
