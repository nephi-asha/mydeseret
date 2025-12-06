package com.mydeseret.mydeseret.service.messaging;

import com.mydeseret.mydeseret.config.TenantContext;
import com.mydeseret.mydeseret.dto.FinancialReportDto;
import com.mydeseret.mydeseret.model.ReportRequest;
import com.mydeseret.mydeseret.repository.ReportRequestRepository;

import com.mydeseret.mydeseret.service.PdfService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class ReportConsumer {

    @Autowired
    private ReportRequestRepository reportRequestRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private com.mydeseret.mydeseret.service.FinancialService financialService;

    @RabbitListener(queues = "${app.rabbitmq.queue.reports}")
    public void generateReport(Map<String, Object> message) {
        String tenantId = (String) message.get("tenantId");
        String reportRequestIdStr = (String) message.get("reportRequestId");
        UUID reportRequestId = UUID.fromString(reportRequestIdStr);
        String reportType = (String) message.get("reportType");
        Map<String, Object> params = (Map<String, Object>) message.get("params");

        // Set Tenant Context
        TenantContext.setCurrentTenant(tenantId);

        ReportRequest request = reportRequestRepository.findById(reportRequestId).orElse(null);
        if (request == null)
            return;

        try {
            request.setStatus("PROCESSING");
            reportRequestRepository.save(request);

            byte[] pdfBytes = null;

            if ("PNL".equals(reportType)) {
                String startDateStr = (String) params.get("startDate");
                String endDateStr = (String) params.get("endDate");
                LocalDateTime startDate = LocalDateTime.parse(startDateStr);
                LocalDateTime endDate = LocalDateTime.parse(endDateStr);

                FinancialReportDto report = financialService.generatePnl(startDate.toLocalDate(),
                        endDate.toLocalDate());

                pdfBytes = pdfService.generatePnlPdf(report, "My Business");
            }

            // Save to File System
            String fileName = "report_" + reportRequestId + ".pdf";
            File file = new File(System.getProperty("java.io.tmpdir"), fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(pdfBytes);
            }

            request.setStatus("COMPLETED");
            request.setFileUrl(file.getAbsolutePath());
            request.setCompletedAt(LocalDateTime.now());
            reportRequestRepository.save(request);

        } catch (Exception e) {
            request.setStatus("FAILED");
            request.setErrorMessage(e.getMessage());
            reportRequestRepository.save(request);
        } finally {
            TenantContext.clear();
        }
    }
}
