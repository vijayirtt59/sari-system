package com.sari.system.api;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/doc-images")
@CrossOrigin
public class DocImageController {

    @PostMapping("/upload")
    public Map<String, String> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String fileName =
                UUID.randomUUID()
                        + "_"
                        + file.getOriginalFilename();

        Path uploadPath =
                Paths.get("uploads/doc");

        Files.createDirectories(uploadPath);

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return Map.of(
                "fileName",
                fileName
        );
    }

    @GetMapping
    public List<String> listImages()
            throws IOException {

        Path uploadPath =
                Paths.get("uploads/doc");

        if (!Files.exists(uploadPath)) {
            return List.of();
        }

        try (Stream<Path> stream =
                     Files.list(uploadPath)) {

            return stream
                    .filter(Files::isRegularFile)
                    .map(path ->
                            path.getFileName()
                                    .toString())
                    .sorted()
                    .toList();
        }
    }
}
