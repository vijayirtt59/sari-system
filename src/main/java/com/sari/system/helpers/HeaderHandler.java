package com.sari.system.helpers;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class HeaderHandler implements IEventHandler {

    private final String title;
    private final String name;
    private final String code;
    private final LocalDate date;

    public HeaderHandler(
            String title,
            String name,
            String code,
            LocalDate date
    ) {
        this.title = title;
        this.name = name;
        this.code = code;
        this.date = date;
    }

    @Override
    public void handleEvent(Event event) {

        PdfDocumentEvent docEvent =
                (PdfDocumentEvent) event;

        PdfDocument pdf =
                docEvent.getDocument();

        PdfPage page =
                docEvent.getPage();

        int pageNum =
                pdf.getPageNumber(page);

        Rectangle pageSize =
                page.getPageSize();

        PdfCanvas pdfCanvas = new PdfCanvas(
                page.newContentStreamBefore(),
                page.getResources(),
                pdf
        );

        Canvas canvas =
                new Canvas(pdfCanvas, pageSize);

        // =====================================================
        // ✅ MAIN TABLE
        // =====================================================

        Table main =
                new Table(new float[]{2, 5, 3});

        main.useAllAvailableWidth();

        float totalWidth =
                pageSize.getWidth() - 80;

        float headerY =
                pageSize.getTop() - 130;

        main.setFixedPosition(
                40,
                headerY,
                totalWidth
        );

        main.setBorder(new DoubleBorder(0.8f));

        main.setPadding(4);

        float cellHeight = 75;

        // =====================================================
        // ✅ LEFT LOGO
        // =====================================================

        Image logo = null;

        try {

            InputStream is =
                    getClass()
                            .getClassLoader()
                            .getResourceAsStream("logo.png");

            if (is != null) {

                logo = new Image(
                        ImageDataFactory.create(
                                is.readAllBytes()
                        )
                );
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        if (logo != null) {

            logo.setAutoScale(true);

            logo.setMaxHeight(65);
        }

        Cell left = new Cell()

                .add(logo)

                .setMinHeight(cellHeight)

                .setVerticalAlignment(
                        VerticalAlignment.MIDDLE
                )

                .setTextAlignment(
                        TextAlignment.CENTER
                )

                .setBorderRight(
                        new SolidBorder(1f)
                )

                .setBorderLeft(Border.NO_BORDER)

                .setBorderTop(Border.NO_BORDER)

                .setBorderBottom(Border.NO_BORDER);

        main.addCell(left);

        // =====================================================
        // ✅ CENTER
        // =====================================================

        Table centerTable = new Table(1);

        centerTable.setWidth(
                UnitValue.createPercentValue(100)
        );

        Cell topCell = new Cell()

                .add(
                        new Paragraph(title)
                                .setBold()
                                .setFontSize(10)
                                .setTextAlignment(
                                        TextAlignment.CENTER
                                )
                )

                .setBorderBottom(
                        new SolidBorder(1f)
                )

                .setBorderTop(Border.NO_BORDER)

                .setBorderLeft(Border.NO_BORDER)

                .setBorderRight(Border.NO_BORDER)

                .setPaddingBottom(4);

        centerTable.addCell(topCell);

        Cell bottomCell = new Cell()

                .add(
                        new Paragraph(name)
                                .setBold()
                                .setFontSize(11)
                                .setTextAlignment(
                                        TextAlignment.CENTER
                                )
                )

                .setBorder(Border.NO_BORDER)

                .setPaddingTop(4);

        centerTable.addCell(bottomCell);

        Cell center = new Cell()

                .add(centerTable)

                .setMinHeight(cellHeight)

                .setMaxHeight(cellHeight)

                .setVerticalAlignment(
                        VerticalAlignment.MIDDLE
                )

                .setBorderRight(
                        new SolidBorder(1f)
                )

                .setBorderLeft(Border.NO_BORDER)

                .setBorderTop(Border.NO_BORDER)

                .setBorderBottom(Border.NO_BORDER);

        main.addCell(center);

        // =====================================================
        // ✅ RIGHT INFO TABLE
        // =====================================================

        Table inner =
                new Table(new float[]{3, 5});

        inner.setWidth(
                UnitValue.createPercentValue(100)
        );

        float rowHeight = 13;

        inner.addCell(
                createCell("Código", true)
                        .setMinHeight(rowHeight)
        );

        inner.addCell(
                createCell(
                        code.replace(" ", "\u00A0"),
                        false
                ).setMinHeight(rowHeight)
        );

        inner.addCell(
                createCell("Versión", true)
                        .setMinHeight(rowHeight)
        );

        inner.addCell(
                createCell("1", false)
                        .setMinHeight(rowHeight)
        );

        inner.addCell(
                createCell("Fecha", true)
                        .setMinHeight(rowHeight)
        );

        inner.addCell(
                createCell(safeDate(date), false)
                        .setMinHeight(rowHeight)
        );

        inner.addCell(
                createCell("Página", true)
                        .setMinHeight(rowHeight)
        );

        inner.addCell(
                createCell(
                        String.valueOf(pageNum),
                        false
                ).setMinHeight(rowHeight)
        );

        Cell right = new Cell()

                .add(inner)

                .setHeight(cellHeight)

                .setVerticalAlignment(
                        VerticalAlignment.MIDDLE
                )

                .setBorder(Border.NO_BORDER)

                .setMarginLeft(-5);

        main.addCell(right);

        // =====================================================
        // ✅ DRAW HEADER
        // =====================================================

        canvas.add(main);

        canvas.close();
    }

    private Cell createCell(
            String text,
            boolean bold
    ) {

        Paragraph p = new Paragraph(text)

                .setFontSize(9)

                .setMargin(0)

                .setMultipliedLeading(1);

        if (bold) {

            p.setBold();
        }

        return new Cell()

                .add(p)

                .setPadding(2)

                .setVerticalAlignment(
                        VerticalAlignment.MIDDLE
                )

                .setTextAlignment(
                        TextAlignment.LEFT
                )

                .setBorder(
                        new SolidBorder(0.6f)
                );
    }

    private String safeDate(LocalDate date) {

        if (date == null) {
            return "-";
        }

        return date.toString();
    }

    private String formatDocumentDate(LocalDate date) {

        if (date == null) {
            return "";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy",
                        new Locale("es", "MX")
                );

        String formatted = date.format(formatter);

        return formatted.substring(0, 3)
                + Character.toUpperCase(
                formatted.charAt(3))
                + formatted.substring(4);
    }

}

