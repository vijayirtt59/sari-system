package com.sari.system.api;

import com.sari.system.application.ProService;
import com.sari.system.domain.Pro;
import com.sari.system.dto.ProRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pro")
public class ProController {


    private final ProService proService;

    public ProController(ProService proService) {
        this.proService = proService;
    }


    // ✅ CREATE
    @PostMapping
    public Pro create(@RequestBody ProRequest req) {
        return proService.create(req);
    }

    // ✅ LIST
    @GetMapping
    public List<Pro> list() {
        return proService.getAll();
    }


    @PutMapping("/{code}")
    public Pro update(@PathVariable String code, @RequestBody ProRequest req) {
        return proService.updateByCode(code, req);
    }


    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        proService.delete(id);
    }

    // ✅ PREVIEW (HTML ONLY)
    @PostMapping("/preview")
    public String preview(@RequestBody ProRequest req) {
        return proService.preview(req);
    }


    @GetMapping("/{code}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable String code) throws Exception {

        byte[] pdf = proService.generatePdf(code);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=" + code + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    @PostMapping("/{code}/action")
    public Pro action(@PathVariable String code,
                      @RequestParam String action,
                      @RequestParam String user) {

        return proService.applyAction(code, action, user);
    }



}
