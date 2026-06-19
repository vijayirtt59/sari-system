package com.sari.system.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poNumber;

    private LocalDate date;

    // ✅ PRODUCT DETAILS
    private String product;
    private String origin;
    private String producer;
    private String grade;

    private String quantity;
    private String price;

    private String packaging;
    private String logistics;

    private String incoterm;
    private String credit;
    private String shipment;

    @Column(length = 2000)
    private String notes;

    @Enumerated(EnumType.STRING)
    private PoStatus status;

    private String createdBy;
    private String approvedBy;

    private LocalDateTime createdDate;
    private LocalDateTime approvedDate;

}
