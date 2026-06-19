package com.sari.system.application;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.sari.system.domain.PoStatus;
import com.sari.system.domain.PurchaseOrder;
import com.sari.system.dto.PurchaseOrderRequest;
import com.sari.system.infrastructure.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {


    private final PurchaseOrderRepository repo;

    public List<PurchaseOrder> findAll() {
        return repo.findAll();
    }

    public PurchaseOrder create(PurchaseOrderRequest req) {

        PurchaseOrder po = map(req);

        po.setStatus(PoStatus.DRAFT);

        po.setCreatedDate(LocalDateTime.now());

        po.setCreatedBy(req.getCreatedBy());

        if (req.getPoNumber() == null
                || req.getPoNumber().isEmpty()) {

            po.setPoNumber(generatePoNumber());
        }

        return repo.save(po);
    }

    public String generatePoNumber() {

        String date =
                LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("ddMMyy"));

        long count = repo.count();

        return "PO-" + date + "-" + String.format("%03d", count + 1);
    }

    public PurchaseOrder update(Long id,
                                PurchaseOrderRequest req) {

        PurchaseOrder po = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("PO not found"));

        update(po, req);

        return repo.save(po);
    }

    private PurchaseOrder map(PurchaseOrderRequest req) {

        return PurchaseOrder.builder()
                .poNumber(req.getPoNumber())
                .date(req.getDate())

                .product(req.getProduct())
                .origin(req.getOrigin())
                .producer(req.getProducer())
                .grade(req.getGrade())

                .quantity(req.getQuantity())
                .price(req.getPrice())

                .packaging(req.getPackaging())
                .logistics(req.getLogistics())

                .incoterm(req.getIncoterm())
                .credit(req.getCredit())
                .shipment(req.getShipment())

                .notes(req.getNotes())
                .build();
    }

    private void update(PurchaseOrder po,
                        PurchaseOrderRequest req) {

        po.setPoNumber(req.getPoNumber());
        po.setDate(req.getDate());

        po.setProduct(req.getProduct());
        po.setOrigin(req.getOrigin());
        po.setProducer(req.getProducer());
        po.setGrade(req.getGrade());

        po.setQuantity(req.getQuantity());
        po.setPrice(req.getPrice());

        po.setPackaging(req.getPackaging());
        po.setLogistics(req.getLogistics());

        po.setIncoterm(req.getIncoterm());
        po.setCredit(req.getCredit());
        po.setShipment(req.getShipment());

        po.setNotes(req.getNotes());
    }

    public PurchaseOrder action(Long id,
                                String action,
                                String user) {

        PurchaseOrder po = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("PO not found"));

        switch (action) {

            case "SEND" -> {
                po.setStatus(PoStatus.SENT);
            }

            case "APPROVE" -> {
                po.setStatus(PoStatus.APPROVED);
                po.setApprovedBy(user);
                po.setApprovedDate(LocalDateTime.now());

                // ✅ lock date if missing
                if (po.getDate() == null) {
                    po.setDate(LocalDate.now());
                }
            }

            case "CLOSE" -> {
                po.setStatus(PoStatus.CLOSED);
            }
        }

        return repo.save(po);
    }

    public byte[] generatePdf(Long id) throws Exception {

        PurchaseOrder po = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("PO not found"));

        String html = buildHtml(po);

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);

        ConverterProperties props =
                new ConverterProperties();

        HtmlConverter.convertToPdf(html, pdf, props);

        pdf.close();

        return out.toByteArray();
    }

    private String buildHtml(PurchaseOrder po) {

        return """
    <html>

    <head>
        <style>

        body {
            font-family: Arial;
            font-size: 12px;
            line-height: 1.5;
        }

        h2 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        td {
            padding: 4px;
            vertical-align: top;
        }

        .label {
            font-weight: bold;
            width: 200px;
        }

        .section {
            margin-top: 15px;
        }

        </style>
    </head>

    <body>

    <h2> PURCHASE ORDER # %s </h2>

    <table>

        <tr>
            <td class="label">Product</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Origin</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Producer</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Grade</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Quantity</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Price</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Packaging</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Logistics</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Incoterm</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Credit</td>
            <td>%s</td>
        </tr>

        <tr>
            <td class="label">Shipment</td>
            <td>%s</td>
        </tr>

    </table>

    <!-- ✅ FIXED SECTIONS -->

    <div class="section">

    <b>Invoice To</b><br/>
    Atlanta Química, S.A. de C.V.<br/>
    LLANURA 155 interior 1<br/>
    colonia Jardines del Pedregal de San Angel<br/>
    Alcaldía Coyoacán, CP 04500, Ciudad de México<br/>
    RFC AQU 910809 R68<br/><br/>

    Kindly indicate your tax id in your invoice

    </div>

    <div class="section">

    <b>Required Documents</b>
    <ul>
        <li>Commercial invoice</li>
        <li>Sea way bill</li>
        <li>Certificate of origin</li>
        <li>Certificate of analysis</li>
        <li>MSDS</li>
        <li>Packing list</li>
        <li>Insurance certificate</li>
    </ul>

    </div>

    <div class="section">

    <b>Notify Party</b><br/>
    Corporativo Logistico GEA SA de CV<br/>
    Colima, México<br/>

    </div>

    <div class="section">

    <b>Important Notes</b><br/>
    %s

    </div>

    </body>

    </html>
    """.formatted(

                po.getPoNumber(),
                safe(po.getProduct()),
                safe(po.getOrigin()),
                safe(po.getProducer()),
                safe(po.getGrade()),
                safe(po.getQuantity()),
                safe(po.getPrice()),
                safe(po.getPackaging()),
                safe(po.getLogistics()),
                safe(po.getIncoterm()),
                safe(po.getCredit()),
                safe(po.getShipment()),
                safe(po.getNotes())
        );
    }

    private String safe(String v) {

        return v == null ? "" : v;
    }


}
