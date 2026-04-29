package com.zhufengma.marathon.controller;

import com.zhufengma.marathon.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/export")
@CrossOrigin(origins = "*")
public class ExportController {

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/registrations/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportEventRegistrations(@PathVariable Long eventId) {
        try {
            byte[] excelData = excelExportService.exportEventRegistrations(eventId);
            
            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String fileName = "报名名单_" + dateStr + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename*=UTF-8''" + encodedFileName)
                .contentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
