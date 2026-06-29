package com.sari.system.application;

import com.sari.system.audit.AuditLog;
import com.sari.system.domain.BusinessRole;
import com.sari.system.domain.Pro;
import com.sari.system.domain.ProChange;
import com.sari.system.domain.ProPdfVersion;
import com.sari.system.domain.RegistroItem;
import com.sari.system.domain.SystemRole;
import com.sari.system.domain.User;
import com.sari.system.dto.ProRequest;
import com.sari.system.infrastructure.AuditLogRepository;
import com.sari.system.infrastructure.ProPdfVersionRepository;
import com.sari.system.infrastructure.ProRepository;
import com.sari.system.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProService {


    private final ProRepository proRepository;
    private final PdfService pdfService;
    private final ProPdfVersionRepository proPdfVersionRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;


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

        boolean workflowProvided =
                hasText(req.getPreparedBy())
                        && hasText(req.getReviewedBy())
                        && hasText(req.getApprovedBy());

        if (workflowProvided) {

            p.setPreparedBy(req.getPreparedBy());
            p.setReviewedBy(req.getReviewedBy());
            p.setApprovedBy(req.getApprovedBy());

            p.setPreparedDate(req.getPreparedDate());
            p.setReviewedDate(req.getReviewedDate());
            p.setApprovedDate(req.getApprovedDate());

            p.setStatus("APPROVED");

        } else {

            p.setStatus("DRAFT");
        }

        return saved;
    }

    private boolean hasText(String value) {

        return value != null
                && !value.isBlank();
    }

    // ✅ UPDATE
    public Pro update(Long id, ProRequest req) {

        Pro p = proRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Not found"));
        p.getRegistros().clear();

        if (p.getChanges() != null) {
            p.getChanges().clear();
        }

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

        if (req.getChangeDescription() != null
                && !req.getChangeDescription().isBlank()) {

            if (p.getChanges() == null) {
                p.setChanges(new ArrayList<>());
            }

            int nextVersion =
                    p.getChanges()
                            .stream()
                            .mapToInt(ProChange::getVersion)
                            .max()
                            .orElse(0) + 1;

            ProChange change = new ProChange();

            change.setVersion(nextVersion);
            change.setDescription(req.getChangeDescription());
            change.setChangeDate(LocalDate.now());

            p.getChanges().add(change);
        }


        // ✅ reset workflow after edit
        p.setStatus("PREPARED");

        p.setPreparedBy(req.getUpdatedBy());
        p.setPreparedDate(LocalDate.now());


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

        p.setPreparedBy(req.getPreparedBy());
        p.setReviewedBy(req.getReviewedBy());
        p.setApprovedBy(req.getApprovedBy());

        p.setPreparedDate(req.getPreparedDate());
        p.setReviewedDate(req.getReviewedDate());
        p.setApprovedDate(req.getApprovedDate());

        p.setChanges(

                req.getChanges() == null
                        ? new ArrayList<>()
                        : req.getChanges()
                        .stream()
                        .map(c -> {

                            ProChange item =
                                    new ProChange();

                            item.setVersion(
                                    c.getVersion()
                            );

                            item.setDescription(
                                    c.getDescription()
                            );

                            item.setChangeDate(
                                    c.getChangeDate()
                            );

                            return item;

                        })
                        .toList()
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

        p.setPreparedBy(request.getPreparedBy());
        p.setReviewedBy(request.getReviewedBy());
        p.setApprovedBy(request.getApprovedBy());

        p.setPreparedDate(request.getPreparedDate());
        p.setReviewedDate(request.getReviewedDate());
        p.setApprovedDate(request.getApprovedDate());

        p.setChanges(

                request.getChanges() == null
                        ? new ArrayList<>()
                        : request.getChanges()
                        .stream()
                        .map(c -> {

                            ProChange item =
                                    new ProChange();

                            item.setVersion(
                                    c.getVersion()
                            );

                            item.setDescription(
                                    c.getDescription()
                            );

                            item.setChangeDate(
                                    c.getChangeDate()
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

        //if (!"APPROVED".equals(p.getStatus())) {
        //  throw new RuntimeException("Document not approved");
        //}
        int version = proPdfVersionRepository.findMaxVersion(code).orElse(0) + 1;


        ProPdfVersion proPdfVersion = new ProPdfVersion();
        proPdfVersion.setCode(code);
        proPdfVersion.setVersion(version);
        proPdfVersion.setCreatedBy("user");
        proPdfVersion.setCreatedDate(LocalDate.now().toString());

        proPdfVersionRepository.save(proPdfVersion);


        return pdfService.generatePro(p);
    }

    public Pro applyAction(String code, String action, Long userId) {

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow();

        Pro p = proRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Not found"));


        switch (action) {

            case "PREPARE" -> {

                requireRole(
                        user,
                        SystemRole.PREPARER
                );

                if (!"DRAFT".equals(
                        p.getStatus()
                )) {

                    throw new RuntimeException(
                            "Procedure already prepared."
                    );
                }

                p.setPreparedBy(
                        user.getWorkflowName()
                );

                p.setPreparedDate(
                        LocalDate.now()
                );

                p.setStatus(
                        "PREPARED"
                );

                log(
                        "PREPARED",
                        p.getCode(),
                        user.getWorkflowName()
                );
            }
            case "REVIEW" -> {

                requireRole(
                        user,
                        SystemRole.REVIEWER
                );

                if (!"PREPARED".equals(
                        p.getStatus()
                )) {

                    throw new RuntimeException(
                            "Procedure must be prepared first."
                    );
                }

                validateReview(
                        user,
                        p
                );

                p.setReviewedBy(
                        user.getWorkflowName()
                );

                p.setReviewedDate(
                        LocalDate.now()
                );

                p.setStatus(
                        "REVIEWED"
                );

                log(
                        "REVIEWED",
                        p.getCode(),
                        user.getWorkflowName()
                );
            }

            case "APPROVE" -> {

                requireRole(
                        user,
                        SystemRole.APPROVER
                );

                if (!"REVIEWED".equals(
                        p.getStatus()
                )) {

                    throw new RuntimeException(
                            "Procedure must be reviewed first."
                    );
                }

                validateApprove(
                        user,
                        p
                );

                p.setApprovedBy(
                        user.getWorkflowName()
                );

                p.setApprovedDate(
                        LocalDate.now()
                );

                p.setStatus(
                        "APPROVED"
                );

                log(
                        "APPROVED",
                        p.getCode(),
                        user.getWorkflowName()
                );
            }
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

    public List<Pro> findByRole(
            BusinessRole role
    ) {

        return proRepository
                .findByResponsableRole(role);

    }

    private void requireRole(
            User user,
            SystemRole role
    ) {

        if (!user.getSystemRoles()
                .contains(role)) {

            throw new RuntimeException(
                    "User lacks role: " + role
            );
        }
    }

    private void validateReview(
            User user,
            Pro pro
    ) {

        String currentUser =
                user.getWorkflowName();

        if (currentUser.equals(
                pro.getPreparedBy()
        )) {

            throw new RuntimeException(
                    "You cannot review your own procedure."
            );
        }
    }

    private void validateApprove(
            User user,
            Pro pro
    ) {

        String currentUser =
                user.getWorkflowName();

        if (currentUser.equals(
                pro.getPreparedBy()
        )) {

            throw new RuntimeException(
                    "You cannot approve your own procedure."
            );
        }

        if (currentUser.equals(
                pro.getReviewedBy()
        )) {

            throw new RuntimeException(
                    "Reviewer cannot approve the same procedure."
            );
        }
    }

}
