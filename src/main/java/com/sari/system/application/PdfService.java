package com.sari.system.application;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.sari.system.domain.Section;
import com.sari.system.domain.FormTemplate;
import com.sari.system.domain.Pro;
import com.sari.system.helpers.HeaderHandler;
import com.sari.system.infrastructure.FormTemplateRepository;
import com.sari.system.infrastructure.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class PdfService {


    private final FormTemplateRepository formTemplateRepository;
    private final SectionRepository sectionRepository;

    public byte[] generatePro(Pro pro) {

        String html = buildHtml(pro);

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        PdfWriter writer =
                new PdfWriter(out);

        PdfDocument pdfDoc =
                new PdfDocument(writer);

        // ✅ HEADER EVENT
        pdfDoc.addEventHandler(
                PdfDocumentEvent.END_PAGE,
                new HeaderHandler(
                        pro.getTitle(),
                        pro.getName(),
                        pro.getCode(),
                        pro.getDocumentDate()
                )
        );

        ConverterProperties props =
                new ConverterProperties();

        HtmlConverter.convertToPdf(
                html,
                pdfDoc,
                props
        );

        pdfDoc.close();

        return out.toByteArray();
    }


    public String buildHtml(Pro v) {

        String today = java.time.LocalDate.now().toString();

        return """
                <html>
                <head>
                <style>
                                
                @page {
                    margin: 180px 40px 60px 40px;
                }
                                

                body {
                    font-family: "Times New Roman", serif;
                    font-size: 11pt;
                    line-height: 1.6;
                    text-align: justify;
                    padding: 20px;
                    color: black;
                }

                .header-table {
                    width: 100%%;
                    border-collapse: collapse;
                    border: 1px solid black;
                }

                .header-table td {
                    border: 1px solid black;
                    padding: 5px;
                }

                .inner-table {
                    width: 100%%;
                    border-collapse: collapse;
                }

                .inner-table td {
                    border: 1px solid black;
                    padding: 4px;
                }

                .signature-table {
                    width: 100%%;
                    border-collapse: collapse;
                    border: 1px solid black;
                    margin-top: 10px;
                }

                .signature-table th, .signature-table td {
                    border: 1px solid black;
                    text-align: center;
                    padding: 5px;
                }

                /* ✅ CHANGE TABLE */
                .change-table {
                    width: 100%%;
                    border-collapse: collapse;
                    border: 1px solid black;
                    margin-top: 10px;
                }

                .change-table td, .change-table th {
                    border: 1px solid black;
                    padding: 5px;
                    text-align: center;
                }


                .section-title {
                    font-weight: bold;
                    margin-top: 20px;
                    margin-bottom: 10px;
                    text-decoration: underline;
                    font-size: 16px;
                }

                .section-content {
                    margin-top: 5px;
                }

                .center {
                    text-align: center;
                    font-weight: bold;
                }

                /* NORMAL TABLES */
                                
                .normal-table {
                                
                    width: 100%%;
                                
                    border-collapse: collapse;
                                
                    margin-top: 10px;
                                
                    margin-bottom: 10px;
                }
                                
                .normal-table td,
                .normal-table th {
                                
                    border: 1px solid black;
                                
                    padding: 6px;
                                
                    vertical-align: top;
                }
                                
                /* HEADER */
                                
                .normal-table tr:first-child td,
                .normal-table tr:first-child th {
                                
                    background-color: #d9d9d9;
                                
                    text-align: center;
                                
                    font-weight: bold;
                }
                                
                                
                .normal-table th {
                                
                    background-color: #d9d9d9;
                                
                    text-align: center;
                                
                    font-weight: bold;
                }
                                
                                
                /* BORDERLESS TABLES */
                                
                .borderless-table {
                                
                    width: 100%%;
                                
                    border-collapse: collapse;
                                
                    margin-top: 10px;
                                
                    margin-bottom: 10px;
                                
                    table-layout: fixed;
                                
                    text-align: left;
                }
                                
                .borderless-table td,
                .borderless-table th {
                                
                    border: none !important;
                                
                    padding: 4px 6px;
                                
                    vertical-align: top;
                                
                    word-break: break-word;
                                
                    overflow-wrap: break-word;
                                
                    text-align: left;
                }
                                
                .borderless-table td:first-child {
                                
                    width: 180px;
                                
                    font-weight: bold;
                }
                                
                /* CKEDITOR TABLE WRAPPER */
                                
                figure.table {
                                
                    margin: 0;
                                
                    padding: 0;
                }
                  
                                
                </style>
                </head>

                <body>

                <!-- ✅ SIGNATURE TABLE -->
                <table class="signature-table">
                <tr>
                    <th>ELABORADO POR</th>
                    <th>REVISADO POR</th>
                    <th>APROBADO POR</th>
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

                <!-- ✅ CHANGE TABLE -->
                <table class="change-table">
                <tr>
                    <th>CAMBIO</th>
                    <th>VERSION</th>
                    <th>FECHA</th>
                </tr>
                <tr>
                    <td>Emisión inicial</td>
                    <td>%s</td>
                    <td>%s</td>
                </tr>
                </table>

                <!-- ✅ PAGE 2 CONTENT -->

                <div class="section-title">I. OBJETIVO</div>
                <div class="section-content">%s</div>

                <div class="section-title">II. ALCANCE</div>
                <div class="section-content">%s</div>

                <div class="section-title">III. PROCEDIMIENTO</div>
                %s

                <div class="section-title">IV. REGISTROS</div>
                <div class="section-content">%s</div>

                </body>
                </html>
                """.formatted(


                // SIGNATURE
                safe(v.getPreparedBy()),
                safe(v.getReviewedBy()),
                safe(v.getApprovedBy()),

                safeDate(v.getPreparedDate()),
                safeDate(v.getReviewedDate()),
                safeDate(v.getApprovedDate()),


                // CHANGE TABLE DATA
                v.getVersion(),
                today,

                // CONTENT (PAGE 2)
                normalizeTables(safe(v.getObjetivo())),
                normalizeTables(safe(v.getAlcance())),
                buildProcedimiento(v),
                normalizeTables(safe(v.getRegistros()))
        );
    }


    private String safe(Object val) {
        return val == null ? "" : val.toString();
    }


    private String safeDate(LocalDate d) {
        return d == null ? "" : d.toString();
    }


    private String normalizeTables(String html) {

        if (html == null || html.isBlank()) {
            return "";
        }

        Document doc = Jsoup.parseBodyFragment(html);

        Elements tables = doc.select("table");

        for (Element table : tables) {

            Elements rows = table.select("tr");

            boolean isBorderless =
                    rows.size() <= 10 &&
                            rows.stream().allMatch(r ->
                                    r.select("td, th").size() == 2
                            );

            // ✅ CONVERT SINGLE-CELL ROWS INTO HEADER ROWS

            for (Element row : rows) {

                Elements cols = row.select("td");

                if (cols.size() == 1) {

                    String text = cols.get(0).text().trim();

                    // ✅ detect section-like labels
                    if (
                            text.toLowerCase().contains("sección") ||
                                    text.toLowerCase().contains("apartado") ||
                                    text.toLowerCase().contains("capítulo")
                    ) {

                        Element td = cols.get(0);

                        Element th = new Element("th");

                        th.attr("colspan", "2");

                        th.html(td.html());

                        td.replaceWith(th);
                    }
                }
            }

            table.removeAttr("style");

            if (isBorderless) {

                table.addClass("borderless-table");

            } else {

                table.addClass("normal-table");
            }
        }

        return doc.body().html();
    }


    private String buildProcedimiento(Pro p) {

        String html = normalizeTables(
                safe(p.getProcedimiento())
        );

        html = html.replace(
                "[[FIRMAS]]",
                """
                <div style='text-align:center;margin-top:10px;margin-bottom:10px;'>
        
                    <img
                        src='http://localhost:8080/uploads/templates/firmas.png'
                        style='
                            width:50%;
                            border:1px solid #999;
                        '
                    />
        
                </div>
                """
        );

        html = html.replace(
                "[[CONTROL_CAMBIOS]]",
                """
                <div style='text-align:center;margin-top:10px;margin-bottom:10px;'>
        
                    <img
                        src='http://localhost:8080/uploads/templates/control-cambios.png'
                        style='
                            width:50%;
                            border:1px solid #999;
                        '
                    />
        
                </div>
                """
        );

        html = html.replace(
                "[[REGISTROS]]",
                """
                <div style='text-align:center;margin-top:10px;margin-bottom:10px;'>
        
                    <img
                        src='http://localhost:8080/uploads/templates/registros.png'
                        style='
                            width:50%;
                            border:1px solid #999;
                        '
                    />
        
                </div>
                """
        );

        // =====================================================
        // ✅ FORM REPLACEMENTS
        // =====================================================

        Pattern formPattern =
                Pattern.compile("\\[\\[FORM:(.*?)]]");

        Matcher formMatcher = formPattern.matcher(html);

        StringBuffer formBuffer = new StringBuffer();

        while (formMatcher.find()) {

            String code = formMatcher.group(1).trim();

            FormTemplate form =
                    formTemplateRepository
                            .findByCode(code)
                            .orElse(null);

            if (form == null) {

                formMatcher.appendReplacement(
                        formBuffer,
                        "<div style='color:red'>" +
                                "Missing Form: " + code +
                                "</div>"
                );

                continue;
            }

            String replacement = """
            <div style='text-align:center;margin-top:20px;margin-bottom:20px;'>

                <h4>
                    %s - %s
                </h4>

                <img
                    src='http://localhost:8080%s'
                    style='
                        width:50%%;
                        border:1px solid #999;
                    '
                />

            </div>
            """.formatted(
                    form.getCode(),
                    form.getTitle(),
                    form.getImageUrl()
            );

            formMatcher.appendReplacement(
                    formBuffer,
                    Matcher.quoteReplacement(replacement)
            );
        }

        formMatcher.appendTail(formBuffer);

        // =====================================================
// ✅ SECTION REPLACEMENTS
// =====================================================

        Pattern sectionPattern =
                Pattern.compile("\\[\\[SECTION:(.*?)]]");

        Matcher sectionMatcher =
                sectionPattern.matcher(formBuffer.toString());

        StringBuffer finalBuffer =
                new StringBuffer();

        while (sectionMatcher.find()) {

            String code =
                    sectionMatcher.group(1).trim();

            Section section =
                    sectionRepository
                            .findByCode(code)
                            .orElse(null);

            if (section == null) {

                sectionMatcher.appendReplacement(
                        finalBuffer,
                        "<div style='color:red'>" +
                                "Missing Section: " + code +
                                "</div>"
                );

                continue;
            }

            String replacement = """
                    <div style='text-align:center;margin-top:20px;margin-bottom:20px;'>

                        <h4>
                            %s - %s
                        </h4>

                        
                                      <img
                                                  src='http://localhost:8080%s'
                                                  style='
                                                      width:30%%;
                                                      max-width:220px;
                                                      border:1px solid #999;
                                                      display:block;
                                                      margin:auto;
                                                  '
                                              />
                                      

                    </div>
                    """.formatted(
                    section.getCode(),
                    section.getTitle(),
                    section.getImageUrl()
            );

            sectionMatcher.appendReplacement(
                    finalBuffer,
                    Matcher.quoteReplacement(replacement)
            );
        }

        sectionMatcher.appendTail(finalBuffer);

        return finalBuffer.toString();
    }

    public byte[] generateSectionPdf(Section s) {

        String content =
                normalizeTables(
                        safe(s.getContent())
                );

        content = content.replace(
                "[[SIGNATURE_TABLE]]",
                buildEmptySignatureTable()
        );


        String html = """
                <html>
                <head>
                <style>

                @page {
                    margin: 200px 40px 60px 40px;
                }

                body {
                    font-family: Arial;
                    font-size: 11pt;
                    line-height: 1.6;
                    text-align: justify;
                    color: black;
                }

                p {
                    margin: 8px 0;
                }

                li {
                    margin-bottom: 5px;
                }
                                
                ul, ol {
                    margin-top: 5px;
                    margin-bottom: 10px;
                }
                ol {
                    padding-left: 20px;
                }
                                
                ol li {
                    margin-bottom: 6px;
                }
                                
                /* ✅ FIRST LEVEL */
                ol {
                    list-style-type: decimal;
                }
                                
                /* ✅ SECOND LEVEL */
                ol ol {
                    list-style-type: lower-alpha;
                }
                                
                /* ✅ THIRD LEVEL */
                ol ol ol {
                    list-style-type: lower-roman;
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
                .signature-table {
                    width: 100%%;
                    border-collapse: collapse;
                    border: 1px solid black;
                    margin-top: 10px;
                }
                                
                .signature-table th,
                .signature-table td {
                                
                    border: 1px solid black;
                                
                    text-align: center;
                                
                    padding: 5px;
                                
                    vertical-align: middle;
                }
                                
                /* NORMAL TABLES */
                                
                .normal-table {
                                
                    width: 100%%;
                                
                    border-collapse: collapse;
                                
                    margin-top: 10px;
                                
                    margin-bottom: 10px;
                }
                                
                .normal-table td,
                .normal-table th {
                                
                    border: 1px solid black;
                                
                    padding: 6px;
                                
                    vertical-align: top;
                }
                                
                /* HEADER ROW */
                                
                .normal-table tr:first-child td,
                .normal-table tr:first-child th {
                                
                    background-color: #d9d9d9;
                                
                    text-align: center;
                                
                    font-weight: bold;
                }
                                
                /* BORDERLESS TABLES */
                                
                .borderless-table {
                                
                    width: 100%%;
                                
                    border-collapse: collapse;
                                
                    margin-top: 10px;
                                
                    margin-bottom: 10px;
                                
                    table-layout: fixed;
                                
                    text-align: left;
                }
                                
                .borderless-table td,
                .borderless-table th {
                                
                    border: none !important;
                                
                    padding: 4px 6px;
                                
                    vertical-align: top;
                                
                    text-align: left;
                }
                                
                .borderless-table td:first-child {
                                
                    width: 180px;
                                
                    font-weight: bold;
                }
                                
                figure.table {
                                
                    margin: 0;
                                
                    padding: 0;
                }

                </style>
                </head>

                <body>

                %s

                </body>
                </html>
                """.formatted(content);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);

        pdfDoc.addEventHandler(
                PdfDocumentEvent.END_PAGE,
                new HeaderHandler(s.getTitle(), s.getName(), s.getCode(), s.getDocumentDate())
        );

        ConverterProperties props = new ConverterProperties();

        HtmlConverter.convertToPdf(html, pdfDoc, props);

        pdfDoc.close();

        return out.toByteArray();
    }

    public String generateSectionPreview(Section s) throws Exception {

        // ✅ Generate PDF first
        byte[] pdfBytes = generateSectionPdf(s);

        String baseName =
                s.getCode().replaceAll("\\s+", "_");

        Path pdfPath = Paths.get(
                "uploads/section-images/" + baseName + ".pdf"
        );

        Path imagePath = Paths.get(
                "uploads/section-images/" + baseName + ".png"
        );

        Files.createDirectories(pdfPath.getParent());

        // ✅ Save PDF
        Files.write(pdfPath, pdfBytes);

        // ✅ Convert first page PDF -> PNG
        PDDocument document =
                PDDocument.load(pdfPath.toFile());

        PDFRenderer renderer =
                new PDFRenderer(document);

        BufferedImage image =
                renderer.renderImageWithDPI(0, 150);

        ImageIO.write(
                image,
                "png",
                imagePath.toFile()
        );

        document.close();

        // ✅ Public URL
        return "/uploads/section-images/" + baseName + ".png";
    }

    private String buildEmptySignatureTable() {

        return """
        <table class='signature-table'>

            <tr>
                <th>ELABORADO POR</th>
                <th>REVISADO POR</th>
                <th>APROBADO POR</th>
            </tr>

            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>

            <tr>
                <td>Nombre y firma</td>
                <td>Nombre y firma</td>
                <td>Nombre y firma</td>
            </tr>

            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>

        </table>
        """;
    }

}


