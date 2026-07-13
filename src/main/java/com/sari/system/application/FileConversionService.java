package com.sari.system.application;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileConversionService {

    @Value("${libreoffice.path}")
    private String libreOfficePath;


    public void convertOfficeToPdf(
            String inputPath,
            String outDir
    ) throws Exception {

        ProcessBuilder pb =
                new ProcessBuilder(

                        libreOfficePath,

                        "--headless",

                        "--convert-to",

                        "pdf",

                        inputPath,

                        "--outdir",

                        outDir
                );

        pb.redirectErrorStream(true);

        Process process =
                pb.start();

        int exitCode =
                process.waitFor();

        if (exitCode != 0) {

            throw new RuntimeException(
                    "Office → PDF failed"
            );
        }
    }

    // ✅ Convert PDF → Image (HIGH QUALITY)
    public void convertPdfToImage(String pdfPath, String imagePath) throws Exception {

        File pdfFile = new File(pdfPath);

        if (!pdfFile.exists()) {
            throw new RuntimeException("❌ PDF file not found: " + pdfPath);
        }

        PDDocument document = PDDocument.load(pdfFile);
        PDFRenderer renderer = new PDFRenderer(document);

        // ✅ Increase DPI for better clarity
        BufferedImage image = renderer.renderImageWithDPI(0, 600);

        ImageIO.write(image, "PNG", new File(imagePath));

        document.close();
    }


}
