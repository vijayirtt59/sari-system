package com.sari.system.application;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.sari.system.domain.Docs;
import com.sari.system.domain.DocumentChange;
import com.sari.system.helpers.HeaderHandler;
import com.sari.system.infrastructure.DocRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasText;

@Service
public class DocServiceImpl implements DocService {


    @Value("${app.base-url}")
    private String baseUrl;

    private final DocRepository repo;

    public DocServiceImpl(DocRepository repo) {
        this.repo = repo;
    }

    // ✅ CREATE
    @Override
    public Docs create(Docs docs) {
        boolean approved =
                hasText(docs.getPreparedBy())
                        &&
                        hasText(docs.getReviewedBy())
                        &&
                        hasText(docs.getApprovedBy());

        docs.setStatus(
                approved
                        ? "APPROVED"
                        : "DRAFT"
        );
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

        DocumentChange change =
                new DocumentChange();

        change.setVersion(existing.getVersion() + 1);

        change.setDescription(
                doc.getChangeDescription()
        );

        change.setChangeDate(
                LocalDate.now()
        );

        existing.getChanges().add(change);

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

        String workflowTable =
                buildWorkflowTable(docs);

        String changeTable =
                buildChangeTable(docs);

        String content =
                replaceImages(
                        safe(docs.getContent())
                );


        String html = """
                <html>
                <head>
                <style>

                @page {
                    margin: 200px 50px 70px 50px;
                }

                body {
                    font-family: Arial;
                    font-size: 15px;
                    line-height: 1.6;
                    text-align: justify;
                }
                                
                table {
                    border-collapse: collapse;
                }
                                
                table td,
                table th {
                    border: 1px solid black;
                    padding: 5px;
                }
                                
                table td {
                    text-align: left;
                    vertical-align: top;
                }
                                
                table th {
                    text-align: center;
                    vertical-align: middle;
                }
                                
                figure.table {
                    margin-top: 10px;
                    margin-bottom: 10px;
                }              
                                
                table {
                    page-break-inside: auto;
                }
                                
                tr {
                    page-break-inside: avoid;
                    page-break-after: auto;
                }
                                
                td {
                    page-break-inside: avoid;
                }
                                
                th {
                    page-break-inside: avoid;
                }
                                
                </style>
                </head>

                <body>
                                
                %s
                                
                %s
                                
                <div style="page-break-before:always;"></div>
                                
                %s
                                
                </body>
                </html>
                """.formatted(
                workflowTable,
                changeTable,
                content
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);

        pdfDoc.addEventHandler(
                PdfDocumentEvent.END_PAGE,
                new HeaderHandler(docs.getTitle(), docs.getName(), docs.getCode(), docs.getDate())
        );

        ConverterProperties props = new ConverterProperties();

        props.setBaseUri(
                baseUrl
        );


        HtmlConverter.convertToPdf(html, pdfDoc, props);

        pdfDoc.close();

        return out.toByteArray();
    }

    private String safe(Object val) {
        return val == null ? "" : val.toString();
    }

    private String buildWorkflowTable(Docs docs) {

        return """
                    
                                                                                         <table border="1"
                                                                                                    cellspacing="0"
                                                                                                    cellpadding="5"
                                                                                                    style="
                                                                                                         border-collapse:collapse;
                                                                                                         margin-top:60px;
                                                                                                         margin-bottom:25px;
                                                                                                    ">

                    <tr>
                        <th width="300">ELABORADO POR</th>
                        <th width="300">REVISADO POR</th>
                        <th width="300">APROBADO POR</th>
                    </tr>

                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                    </tr>

                    <tr>
                        <td>Nombre y firma</td>
                        <td>Nombre y firma</td>
                        <td>Nombre y firma</td>
                    </tr>

                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                    </tr>

                </table>
                """
                .formatted(
                        safe(docs.getPreparedBy()),
                        safe(docs.getReviewedBy()),
                        safe(docs.getApprovedBy()),
                        safeDate(docs.getPreparedDate()),
                        safeDate(docs.getReviewedDate()),
                        safeDate(docs.getApprovedDate())
                );
    }

    private String safeDate(LocalDate date) {

        if (date == null) {
            return "";
        }

        return date.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    }

    private String buildChangeTable(Docs docs) {

        StringBuilder html = new StringBuilder();

        html.append("""
                    <table border="1"
                                                                                cellspacing="0"
                                                                                cellpadding="5"
                                                                                style="
                                                                                     border-collapse:collapse;
                                                                                     margin-top:60px;
                                                                                ">

                        <tr>
                            <th width="200">VERSION</th>
                            <th width="500">CAMBIO</th>
                            <th width="200">FECHA</th>
                        </tr>
                """);

        if (docs.getChanges() != null) {

            for (DocumentChange c : docs.getChanges()) {

                html.append("""
                            <tr>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                        """
                        .formatted(
                                safe(c.getVersion()),
                                safe(c.getDescription()),
                                safeDate(c.getChangeDate())
                        ));
            }
        }

        html.append("</table>");

        return html.toString();
    }


    private String replaceImages(String html) {

        if (html == null) {
            return "";
        }

        Pattern pattern =
                Pattern.compile("\\[IMAGE:([^\\]]+)]");

        Matcher matcher =
                pattern.matcher(html);

        StringBuffer result =
                new StringBuffer();

        while (matcher.find()) {

            String fileName =
                    matcher.group(1);

            String imgTag = """
                    <div style="text-align:center; margin:15px 0;">
                        <img
                            src="%s/doc/%s"
                            
                                                                     style="
                                                                         max-width:500px;
                                                                         height:auto;
                                                                         margin-top:10px;
                                                                         margin-bottom:10px;
                                                                     "
                        />
                    </div>
                    """.formatted(baseUrl, fileName);

            matcher.appendReplacement(
                    result,
                    Matcher.quoteReplacement(imgTag)
            );
        }

        matcher.appendTail(result);

        return result.toString();
    }


}
