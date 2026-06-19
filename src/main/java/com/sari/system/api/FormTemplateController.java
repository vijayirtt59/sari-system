package com.sari.system.api;


import com.sari.system.application.FormTemplateService;
import com.sari.system.domain.FormTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormTemplateController {


    private final FormTemplateService service;

    @GetMapping
    public List<FormTemplate> getAll() {
        return service.getAll();
    }

    @PostMapping("/upload")
    public Map<String, String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("code") String code,
            @RequestParam("title") String title
    ) throws Exception {

        FormTemplate form = service.upload(file, code, title);

        return Map.of(
                "imageUrl", form.getImageUrl(),
                "fileUrl", form.getFileUrl()
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
