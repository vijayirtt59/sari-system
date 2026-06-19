package com.sari.system.application;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.sari.system.domain.Docs;
import com.sari.system.helpers.HeaderHandler;
import com.sari.system.infrastructure.DocRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class DocServiceImpl implements DocService {


    private final DocRepository repo;

    public DocServiceImpl(DocRepository repo) {
        this.repo = repo;
    }

    // ✅ CREATE
    @Override
    public Docs create(Docs docs) {
        return repo.save(docs);
    }

    // ✅ UPDATE
    @Override
    public Docs update(Long id, Docs doc) {

        Docs existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doc not found"));

        existing.setCode(doc.getCode());
        existing.setTitle(doc.getTitle());
        existing.setName(doc.getName());
        existing.setContent(doc.getContent());
        existing.setDate(doc.getDate());

        return repo.save(existing);
    }

    // ✅ DELETE
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ LIST
    @Override
    public List<Docs> getAll() {
        return repo.findAllByOrderByCodeAsc();
    }

    // ✅ GET BY CODE
    @Override
    public Docs getByCode(String code) {
        return repo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Doc not found"));
    }

    // ✅ PDF GENERATION
    @Override
    public byte[] generateDocsPdf(String code) throws Exception {

        Docs docs = getByCode(code);

        String html = """
                <html>
                <head>
                <style>

                @page {
                    margin: 200px 50px 70px 50px;
                }

                body {
                    font-family: Arial;
                    font-size: 16px;
                    line-height: 1.6;
                    text-align: justify;
                }

                p {
                    margin: 8px 0;
                }

                ul, ol {
                    margin-left: 25px;
                }

                h1 {
                    font-size: 16px;
                    font-weight: bold;
                    text-decoration: underline;
                }

                h2 {
                    font-size: 14px;
                    font-weight: bold;
                }

                h3 {
                    font-size: 13px;
                    margin-left: 10px;
                }

                </style>
                </head>

                <body>

                %s

                </body>
                </html>
                """.formatted(safe(docs.getContent()));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);

        pdfDoc.addEventHandler(
                PdfDocumentEvent.END_PAGE,
                new HeaderHandler(docs.getTitle(), docs.getName(), docs.getCode(), docs.getDate())
        );

        ConverterProperties props = new ConverterProperties();

        HtmlConverter.convertToPdf(html, pdfDoc, props);

        pdfDoc.close();

        return out.toByteArray();
    }

    private String safe(Object val) {
        return val == null ? "" : val.toString();
    }

}
