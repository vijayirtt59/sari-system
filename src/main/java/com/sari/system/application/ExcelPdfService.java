package com.sari.system.application;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class ExcelPdfService {

    public byte[] convertExcelToPdf(
            File excelFile
    ) throws Exception {

        Workbook workbook =
                WorkbookFactory.create(
                        excelFile
                );

        PDDocument document =
                new PDDocument();

        PDPage page =
                new PDPage();

        document.addPage(page);

        PDPageContentStream content =
                new PDPageContentStream(
                        document,
                        page
                );

        content.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
                ),
                10
        );

        float y = 750;

        for (Sheet sheet : workbook) {

            content.beginText();

            content.newLineAtOffset(
                    40,
                    y
            );

            content.showText(
                    "Sheet: " +
                            sheet.getSheetName()
            );

            content.endText();

            y -= 20;

            for (Row row : sheet) {

                StringBuilder line =
                        new StringBuilder();

                for (Cell cell : row) {

                    line.append(
                            cell.toString()
                    );

                    line.append(" | ");
                }

                content.beginText();

                content.newLineAtOffset(
                        40,
                        y
                );

                String text =
                        line.toString();

                if (text.length() > 120) {
                    text =
                            text.substring(
                                    0,
                                    120
                            );
                }

                content.showText(text);

                content.endText();

                y -= 15;

                if (y < 50) {

                    content.close();

                    page =
                            new PDPage();

                    document.addPage(page);

                    content =
                            new PDPageContentStream(
                                    document,
                                    page
                            );

                    content.setFont(
                            new PDType1Font(
                                    Standard14Fonts.FontName.HELVETICA
                            ),
                            10
                    );

                    y = 750;
                }
            }

            y -= 20;
        }

        content.close();

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        document.save(out);

        document.close();

        workbook.close();

        return out.toByteArray();
    }

}
