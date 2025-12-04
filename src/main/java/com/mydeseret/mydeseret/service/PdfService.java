package com.mydeseret.mydeseret.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.mydeseret.mydeseret.dto.FinancialReportDto;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {

    public byte[] generatePnlPdf(FinancialReportDto report, String businessName) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph(businessName, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Color.GRAY);
            Paragraph subtitle = new Paragraph("Profit & Loss Statement", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20);
            document.add(subtitle);

            // Date Range
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String dateRange = "Period: " + report.getStartDate().format(formatter) + 
                               " to " + report.getEndDate().format(formatter);
            document.add(new Paragraph(dateRange, normalFont));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now().format(formatter), normalFont));
            document.add(Chunk.NEWLINE);

            // Create Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1f});
            table.setSpacingBefore(10);

            // Rows
            addSectionHeader(table, "REVENUE");
            addTableRow(table, "Total Sales", formatCurrency(report.getTotalRevenue()), false);
            
            addSectionHeader(table, "COST OF GOODS SOLD");
            addTableRow(table, "Cost of Materials & Production", "(" + formatCurrency(report.getCostOfGoodsSold()) + ")", false);
            
            // Gross Profit Calculation Row
            addTotalRow(table, "GROSS PROFIT", formatCurrency(report.getGrossProfit()), Color.LIGHT_GRAY);

            addSectionHeader(table, "OPERATING EXPENSES");
            addTableRow(table, "Total Expenses (Rent, Payroll, etc.)", "(" + formatCurrency(report.getTotalExpenses()) + ")", false);

            // Net Profit
            Color profitColor = report.getNetProfit().doubleValue() >= 0 ? new Color(220, 255, 220) : new Color(255, 220, 220);
            addTotalRow(table, "NET PROFIT", formatCurrency(report.getNetProfit()), profitColor);

            // Margin
            document.add(table);
            document.add(Chunk.NEWLINE);
            
            Paragraph margin = new Paragraph("Net Profit Margin: " + report.getProfitMargin(), 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            margin.setAlignment(Element.ALIGN_RIGHT);
            document.add(margin);

            document.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    // --- Helpers ---

    private void addSectionHeader(PdfPTable table, String title) {
        PdfPCell cell = new PdfPCell(new Phrase(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE)));
        cell.setBackgroundColor(Color.DARK_GRAY);
        cell.setColspan(2);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, String label, String value, boolean isBold) {
        Font font = isBold ? FontFactory.getFont(FontFactory.HELVETICA_BOLD) : FontFactory.getFont(FontFactory.HELVETICA);
        
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setPadding(8);
        labelCell.setBorderWidthBottom(0.5f);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setPadding(8);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorderWidthBottom(0.5f);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addTotalRow(PdfPTable table, String label, String value, Color bgColor) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBackgroundColor(bgColor);
        labelCell.setPadding(10);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBackgroundColor(bgColor);
        valueCell.setPadding(10);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String formatCurrency(java.math.BigDecimal amount) {
        if (amount == null) return "0.00";
        return NumberFormat.getCurrencyInstance(new Locale("en", "NG")).format(amount).replace("NGN", "₦");
    }
}