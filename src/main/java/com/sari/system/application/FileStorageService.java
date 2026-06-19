package com.sari.system.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final String PATH = "uploads/";

    public String save(String code, MultipartFile file) throws Exception {

        Files.createDirectories(Paths.get(PATH));

        String fileName = code + ".pdf";

        Path path = Paths.get(PATH + fileName);

        Files.copy(file.getInputStream(), path,
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

}
