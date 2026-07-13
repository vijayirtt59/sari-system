package com.sari.system.application;

import com.sari.system.domain.FormTemplate;
import com.sari.system.infrastructure.FormTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormTemplateService {

    private final FormTemplateRepository repo;
    private final FileConversionService fileConversionService;


    private final String BASE_PATH =
            System.getProperty("user.dir") + File.separator + "uploads" + File.separator;


    public List<FormTemplate> getAll() {
        return repo.findAll();
    }

    public FormTemplate upload(MultipartFile file, String code, String title) throws Exception {

        String originalName =
                file.getOriginalFilename();

        String extension =
                originalName.substring(
                        originalName.lastIndexOf(".") + 1
                ).toLowerCase();

        String sourcePath =
                BASE_PATH +
                        code +
                        "." +
                        extension;
        String pdfPath = BASE_PATH + code + ".pdf";
        String imagePath = BASE_PATH + code + ".png";



// ✅ Ensure folder exists
        File dir = new File(BASE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();   // create folder
        }

// ✅ Save file
        File sourceFile =
                new File(sourcePath);
        file.transferTo(sourceFile);


        // ✅ convert Excel → PDF
        fileConversionService.convertOfficeToPdf(sourcePath, BASE_PATH);

        // ✅ convert PDF → image
        fileConversionService.convertPdfToImage(pdfPath, imagePath);

        // ✅ check existing
        FormTemplate form = repo.findByCode(code)
                .orElse(new FormTemplate());

        form.setCode(code);
        form.setTitle(title);
        form.setFileUrl("/uploads/" + code + "." + extension);
        form.setImageUrl("/uploads/" + code + ".png");

        return repo.save(form);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}
