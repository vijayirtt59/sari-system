package com.sari.system.application;

import com.sari.system.domain.BusinessRole;
import com.sari.system.dto.DashboardOverview;
import com.sari.system.dto.DocumentSummary;
import com.sari.system.dto.MyResponsibilities;
import com.sari.system.dto.PendingItem;
import com.sari.system.infrastructure.DocRepository;
import com.sari.system.infrastructure.FormTemplateRepository;
import com.sari.system.infrastructure.ProRepository;
import com.sari.system.infrastructure.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProRepository proRepository;
    private final DocRepository docRepository;
    private final SectionRepository sectionRepository;
    private final FormTemplateRepository formTemplateRepository;

    public DashboardOverview getOverview(){
        long approved =
                proRepository.countByStatus("APPROVED")
                        + docRepository.countByStatus("APPROVED");

        long draft =
                proRepository.countByStatus("DRAFT")
                        + docRepository.countByStatus("DRAFT");

        long prepared =
                proRepository.countByStatus("PREPARED")
                        + docRepository.countByStatus("PREPARED");

        long reviewed =
                proRepository.countByStatus("REVIEWED")
                        + docRepository.countByStatus("REVIEWED");


        return DashboardOverview.builder()
                .totalPros(
                        proRepository.count()
                )
                .totalDocs(
                        docRepository.count()
                )
                .totalSections(
                        sectionRepository.count()
                )
                .totalForms(
                        formTemplateRepository.count()
                ). approved(approved)
                .prepared(prepared)
                .reviewed(reviewed)
                .draft(draft)
                .build();
    }

    public List<DocumentSummary> getRecent(){
        List<DocumentSummary> result = new ArrayList<>();

        proRepository.findAll()
                .forEach(pro -> result.add(
                        DocumentSummary.builder()
                                .code(pro.getCode())
                                .title(pro.getTitle())
                                .status(pro.getStatus())
                                .type("PRO")
                                .date(pro.getDocumentDate())
                                .build()
                ));

        docRepository.findAll()
                .forEach(doc -> result.add(
                        DocumentSummary.builder()
                                .code(doc.getCode())
                                .title(doc.getTitle())
                                .status(doc.getStatus())
                                .type("DOC")
                                .date(doc.getDate())
                                .build()
                ));

        return result.stream()
                .sorted(
                        Comparator.comparing(
                                DocumentSummary::getDate,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .limit(5)
                .toList();
    }

    public MyResponsibilities getResponsibilities(
            BusinessRole role
    ) {

        long assignedPros =
                proRepository.findAll()
                        .stream()
                        .filter(p ->
                                p.getRegistros() != null &&
                                        p.getRegistros()
                                                .stream()
                                                .anyMatch(r ->
                                                        role.equals(
                                                                r.getResponsableResguardo()
                                                        )
                                                ))
                        .count();

        long assignedRecords =
                proRepository.findAll()
                        .stream()
                        .flatMap(p ->
                                p.getRegistros().stream())
                        .filter(r ->
                                role.equals(
                                        r.getResponsableResguardo()
                                ))
                        .count();

        long pendingReview =
                proRepository.countByStatus(
                        "PREPARED"
                )
                        +
                        docRepository.countByStatus(
                                "PREPARED"
                        );

        long pendingApproval =
                proRepository.countByStatus(
                        "REVIEWED"
                )
                        +
                        docRepository.countByStatus(
                                "REVIEWED"
                        );

        return MyResponsibilities.builder()
                .assignedPros(assignedPros)
                .assignedRecords(assignedRecords)
                .pendingReview(pendingReview)
                .pendingApproval(pendingApproval)
                .build();
    }

    public List<PendingItem> getPendingItems(){
        List<PendingItem> result =
                new ArrayList<>();

        proRepository.findByStatusIn(
                List.of(
                        "PREPARED",
                        "REVIEWED"
                )
        ).forEach(pro -> {

            result.add(
                    PendingItem.builder()
                            .code(pro.getCode())
                            .title(pro.getTitle())
                            .type("PRO")
                            .status(pro.getStatus())
                            .build()
            );

        });

        docRepository.findAll()
                .stream()
                .filter(d ->
                        "PREPARED".equals(
                                d.getStatus()
                        )
                                ||
                                "REVIEWED".equals(
                                        d.getStatus()
                                )
                )
                .forEach(doc -> {

                    result.add(
                            PendingItem.builder()
                                    .code(doc.getCode())
                                    .title(doc.getTitle())
                                    .type("DOC")
                                    .status(doc.getStatus())
                                    .build()
                    );

                });

        return result;
    }

}
