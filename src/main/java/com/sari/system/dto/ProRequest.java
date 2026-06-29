package com.sari.system.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProRequest {

    private String code;
    private String title;
    private String name;
    private String objetivo;
    private String alcance;
    private String procedimiento;
    private List<RegistroItemRequest> registros;
    private List<Long> sectionIds;
    private String updatedBy;
    private LocalDate documentDate;

    private String preparedBy;
    private String reviewedBy;
    private String approvedBy;

    private LocalDate preparedDate;
    private LocalDate reviewedDate;
    private LocalDate approvedDate;

    private List<ProChangeRequest> changes;
    private String changeDescription;

}
