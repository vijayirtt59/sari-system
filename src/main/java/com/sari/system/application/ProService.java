package com.sari.system.application;

import com.sari.system.audit.AuditLog;
import com.sari.system.domain.*;
import com.sari.system.dto.ProRequest;
import com.sari.system.infrastructure.AuditLogRepository;
import com.sari.system.infrastructure.ProPdfVersionRepository;
import com.sari.system.infrastructure.ProRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProService {



    private final ProRepository proRepository;
    private final PdfService pdfService;
    private final ProPdfVersionRepository proPdfVersionRepository;
    private final AuditLogRepository auditLogRepository;


    // ✅ CREATE
    public Pro create(ProRequest req) {

        Pro p = new Pro();

        map(p, req);

        Pro saved =
                proRepository.save(p);

        log(
                "CREATED",
                saved.getCode(),
                req.getUpdatedBy()
        );

        return saved;
    }

    // ✅ UPDATE
    public Pro update(Long id, ProRequest req) {

        Pro p = proRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Not found"));
        p.getRegistros().clear();

        map(p, req);

        Pro saved =
                proRepository.save(p);

        log(
                "UPDATED",
                saved.getCode(),
                req.getUpdatedBy()
        );

        return saved;
    }

    public Pro updateByCode(String code, ProRequest req) {

        System.out.println("UPDATE CODE = " + code);

        Pro p = proRepository.findByCode(code.trim())
                .orElseThrow(() ->
                        new RuntimeException(
                                "PRO not found: " + code
                        ));

        p.setName(req.getName());
        p.setTitle(req.getTitle());
        p.setObjetivo(req.getObjetivo());
        p.setAlcance(req.getAlcance());
        p.setProcedimiento(req.getProcedimiento());
        p.setDocumentDate(
                req.getDocumentDate()
        );

        p.getRegistros().clear();

        p.setRegistros(

                req.getRegistros()

                        .stream()

                        .map(registroItemRequest -> {

                            RegistroItem item =
                                    new RegistroItem();

                            item.setCodigo(
                                    registroItemRequest.getCodigo()
                            );

                            item.setNombre(
                                    registroItemRequest.getNombre()
                            );

                            item.setAlmacenamiento(
                                    registroItemRequest.getAlmacenamiento()
                            );

                            item.setTiempoRetencion(
                                    registroItemRequest.getTiempoRetencion()
                            );

                            item.setResponsableResguardo(
                                    registroItemRequest.getResponsableResguardo()
                            );

                            return item;

                        })

                        .toList()
        );


        // ✅ reset workflow after edit
        p.setStatus("PREPARED");

        p.setReviewedBy(null);
        p.setReviewedDate(null);

        p.setApprovedBy(null);
        p.setApprovedDate(null);

        // ✅ audit
        p.setLastModifiedBy(req.getUpdatedBy());
        p.setLastModifiedDate(LocalDate.now().toString());

        Pro saved =
                proRepository.save(p);

        log(
                "UPDATED",
                saved.getCode(),
                req.getUpdatedBy()
        );

        return saved;
    }


    // ✅ MAPPING
    private void map(Pro p, ProRequest req) {
        p.setCode(req.getCode());
        p.setTitle(req.getTitle());
        p.setName(req.getName());
        p.setObjetivo(req.getObjetivo());
        p.setAlcance(req.getAlcance());
        p.setProcedimiento(req.getProcedimiento());
        p.setRegistros(
                req.getRegistros()
                        .stream()
                        .map(r -> {
                            RegistroItem item =
                                    new RegistroItem();

                            item.setCodigo(
                                    r.getCodigo()
                            );

                            item.setNombre(
                                    r.getNombre()
                            );

                            item.setAlmacenamiento(
                                    r.getAlmacenamiento()
                            );

                            item.setTiempoRetencion(
                                    r.getTiempoRetencion()
                            );

                            item.setResponsableResguardo(
                                    r.getResponsableResguardo()
                            );

                            return item;

                        })
                        .toList()
        );

        p.setDocumentDate(
                req.getDocumentDate()
        );

    }


    // ✅ DELETE
    public void delete(Long id) {
        proRepository.deleteById(id);
    }

    // ✅ LIST
    public List<Pro> getAll() {
        return proRepository.findAll();
    }

    // ✅ PREVIEW HTML
    public String preview(ProRequest request) {

        Pro p = new Pro();

        p.setCode(request.getCode());
        p.setName(request.getName());
        p.setTitle(request.getTitle());
        p.setObjetivo(request.getObjetivo());
        p.setAlcance(request.getAlcance());
        p.setProcedimiento(request.getProcedimiento());
        p.setRegistros(
                request.getRegistros()
                        .stream()
                        .map(r -> {

                            RegistroItem item =
                                    new RegistroItem();

                            item.setCodigo(
                                    r.getCodigo()
                            );

                            item.setNombre(
                                    r.getNombre()
                            );

                            item.setAlmacenamiento(
                                    r.getAlmacenamiento()
                            );

                            item.setTiempoRetencion(
                                    r.getTiempoRetencion()
                            );

                            item.setResponsableResguardo(
                                    r.getResponsableResguardo()
                            );

                            return item;

                        })
                        .toList()
        );

        return pdfService.buildHtml(p);
    }

    // ✅ PDF
    public byte[] generatePdf(String code) throws Exception {

        Pro p = proRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("PRO not found"));

        if (!"APPROVED".equals(p.getStatus())) {
            throw new RuntimeException("Document not approved");
        }
        int version = proPdfVersionRepository.findMaxVersion(code).orElse(0) + 1;


        ProPdfVersion proPdfVersion = new ProPdfVersion();
        proPdfVersion.setCode(code);
        proPdfVersion.setVersion(version);
        proPdfVersion.setCreatedBy("user");
        proPdfVersion.setCreatedDate(LocalDate.now().toString());

        proPdfVersionRepository.save(proPdfVersion);


        return pdfService.generatePro(p);
    }

    public Pro applyAction(String code, String action, String user) {

        Pro p = proRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Not found"));


        switch (action) {

            case "PREPARE":
                if (!p.getStatus().equals("DRAFT"))
                    throw new RuntimeException("Invalid state");

                p.setStatus("PREPARED");
                p.setPreparedBy(user);
                p.setPreparedDate(LocalDate.now());
                log(
                        "PREPARED",
                        p.getCode(),
                        user
                );
                break;

            case "REVIEW":
                if (!p.getStatus().equals("PREPARED"))
                    throw new RuntimeException("Invalid state");

                p.setStatus("REVIEWED");
                p.setReviewedBy(user);
                p.setReviewedDate(LocalDate.now());
                log(
                        "REVIEWED",
                        p.getCode(),
                        user
                );
                break;

            case "APPROVE":
                if (!p.getStatus().equals("REVIEWED"))
                    throw new RuntimeException("Invalid state");

                p.setStatus("APPROVED");
                p.setApprovedBy(user);
                p.setApprovedDate(LocalDate.now());
                log(
                        "APPROVED",
                        p.getCode(),
                        user
                );
                break;
        }

        return proRepository.save(p);
    }

    private void log(
            String action,
            String code,
            String username
    ) {

        auditLogRepository.save(

                AuditLog.builder()
                        .action(action)
                        .documentCode(code)
                        .username(username)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

}
