package com.sari.system.api;


import com.sari.system.application.PdfService;
import com.sari.system.domain.Section;
import com.sari.system.infrastructure.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class SectionController {


    private final SectionRepository repo;
    private final PdfService pdfService;


    @PostMapping
    public Section createOrUpdate(@RequestBody Section s) throws Exception {

        Optional<Section> existing = repo.findByCode(s.getCode());

        Section saved;

        if (existing.isPresent()) {

            // ✅ UPDATE EXISTING
            Section db = existing.get();
            db.setTitle(s.getTitle());
            db.setContent(s.getContent());
            db.setName(s.getName());

            saved = repo.save(db);

        } else {

            // ✅ CREATE NEW
            saved = repo.save(s);
        }

        pdfService.generateSectionPdf(saved);

        String imageUrl =
                pdfService.generateSectionPreview(saved);

        saved.setImageUrl(imageUrl);

        saved = repo.save(saved);

        return saved;
    }

    @PutMapping("/{id}")
    public Section update(@PathVariable Long id,
                          @RequestBody Section updated) throws Exception {

        Section s = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Section not found: " + id
                        )
                );

        s.setCode(updated.getCode());
        s.setTitle(updated.getTitle());
        s.setName(updated.getName());
        s.setContent(updated.getContent());

        Section saved = repo.save(s);

        pdfService.generateSectionPdf(saved);


        String imageUrl =
                pdfService.generateSectionPreview(saved);

        saved.setImageUrl(imageUrl);

        repo.save(saved);


        return saved;
    }


    // ✅ GET ALL
    @GetMapping
    public List<Section> getAll() {
        return repo.findAll();
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public List<Section> search(@RequestParam String q) {
        return repo.findByTitleContainingIgnoreCase(q);
    }


    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateSectionPdf(@PathVariable Long id) throws Exception {

        Section s = repo.findById(id).orElseThrow();

        byte[] pdf = pdfService.generateSectionPdf(s);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=" + s.getCode() + ".pdf")
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
