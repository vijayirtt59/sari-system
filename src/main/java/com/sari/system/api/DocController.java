package com.sari.system.api;


import com.sari.system.application.DocService;
import com.sari.system.domain.Docs;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/docs")
public class DocController {


    private final DocService service;

    public DocController(DocService service) {
        this.service = service;
    }

    // ✅ LIST
    @GetMapping
    public List<Docs> list() {
        return service.getAll();
    }

    // ✅ CREATE
    @PostMapping
    public Docs create(@RequestBody Docs doc) {
        return service.create(doc);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public Docs update(@PathVariable Long id,
                       @RequestBody Docs docs) {
        return service.update(id, docs);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ✅ PDF DOWNLOAD
    @GetMapping("/{code}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable String code) throws Exception {

        byte[] pdf = service.generateDocsPdf(code);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + code + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
