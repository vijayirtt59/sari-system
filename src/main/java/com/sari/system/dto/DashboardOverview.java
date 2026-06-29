package com.sari.system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardOverview {
    private long totalPros;
    private long totalDocs;
    private long totalSections;
    private long totalForms;

    private long approved;
    private long draft;
    private long prepared;
    private long reviewed;
}
