package com.sari.system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyResponsibilities {

    private long assignedPros;

    private long assignedRecords;

    private long pendingReview;

    private long pendingApproval;
}
