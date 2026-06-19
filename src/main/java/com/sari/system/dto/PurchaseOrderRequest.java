package com.sari.system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurchaseOrderRequest {

    private String poNumber;
    private LocalDate date;

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

    private String notes;

    private String createdBy;

}
