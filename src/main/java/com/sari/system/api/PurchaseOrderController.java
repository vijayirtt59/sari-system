package com.sari.system.api;

import com.sari.system.application.PurchaseOrderService;
import com.sari.system.domain.PurchaseOrder;
import com.sari.system.dto.PurchaseOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/po")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    @GetMapping
    public List<PurchaseOrder> all() {
        return service.findAll();
    }

    @PostMapping
    public PurchaseOrder create(
            @RequestBody PurchaseOrderRequest req
    ) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public PurchaseOrder update(
            @PathVariable Long id,
            @RequestBody PurchaseOrderRequest req
    ) {
        return service.update(id, req);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(
            @PathVariable Long id
    ) throws Exception {

        byte[] pdf = service.generatePdf(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=po.pdf")
                .body(pdf);
    }

    @PostMapping("/{id}/action")
    public PurchaseOrder action(
            @PathVariable Long id,
            @RequestParam String action,
            @RequestParam String user
    ) {

        return service.action(id, action, user);
    }


}
