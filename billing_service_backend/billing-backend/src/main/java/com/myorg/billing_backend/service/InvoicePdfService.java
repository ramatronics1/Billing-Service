package com.myorg.billing_backend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.myorg.billing_backend.model.Invoice;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class InvoicePdfService {

    private static final String BASE_DIR = "invoices";

    public String generatePdf(Invoice invoice) {
        try {
            // Ensure directory exists
            File dir = new File(BASE_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = "invoice-" + invoice.getInvoiceNo() + ".pdf";
            String filePath = BASE_DIR + "/" + fileName;

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font bodyFont = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph("INVOICE", titleFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Invoice No: " + invoice.getInvoiceNo(), bodyFont));
            document.add(new Paragraph("Amount: " + invoice.getAmountCents() + " " + invoice.getCurrency(), bodyFont));
            document.add(new Paragraph("Status: " + invoice.getStatus(), bodyFont));

            if (invoice.getIssuedAt() != null) {
                document.add(new Paragraph(
                        "Issued At: " +
                                invoice.getIssuedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        bodyFont
                ));
            }

            if (invoice.getDueAt() != null) {
                document.add(new Paragraph(
                        "Due At: " +
                                invoice.getDueAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        bodyFont
                ));
            }

            document.close();
            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }
    }
}
