package com.zhufengma.marathon.service;

import com.zhufengma.marathon.entity.Event;
import com.zhufengma.marathon.entity.EventGroup;
import com.zhufengma.marathon.entity.Registration;
import com.zhufengma.marathon.entity.User;
import com.zhufengma.marathon.repository.EventGroupRepository;
import com.zhufengma.marathon.repository.EventRepository;
import com.zhufengma.marathon.repository.RegistrationRepository;
import com.zhufengma.marathon.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventGroupRepository eventGroupRepository;

    @Autowired
    private UserRepository userRepository;

    public byte[] exportEventRegistrations(Long eventId) throws IOException {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("赛事不存在"));

        List<Registration> registrations = registrationRepository.findByEventIdAndIsDeleted(eventId, 0);

        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet(event.getName() + " 报名名单");
            
            // 创建标题行样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "序号", "报名ID", "用户名", "真实姓名", "身份证号", "手机号", "邮箱",
                "性别", "出生日期", "组别", "T恤尺码", "紧急联系人", "紧急联系电话",
                "病史", "备注", "报名状态", "审核备注", "报名时间"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 15 * 256);
            }

            // 填充数据
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            int rowNum = 1;
            for (Registration registration : registrations) {
                Row row = sheet.createRow(rowNum++);
                
                User user = userRepository.findById(registration.getUserId()).orElse(null);
                EventGroup group = eventGroupRepository.findById(registration.getGroupId()).orElse(null);

                int colNum = 0;
                row.createCell(colNum++).setCellValue(rowNum - 1); // 序号
                row.createCell(colNum++).setCellValue(registration.getId() != null ? registration.getId().toString() : "");
                row.createCell(colNum++).setCellValue(user != null ? user.getUsername() : "");
                row.createCell(colNum++).setCellValue(user != null ? user.getRealName() : "");
                row.createCell(colNum++).setCellValue(user != null ? maskIdCard(user.getIdCard()) : "");
                row.createCell(colNum++).setCellValue(user != null ? user.getPhone() : "");
                row.createCell(colNum++).setCellValue(user != null ? user.getEmail() : "");
                row.createCell(colNum++).setCellValue(user != null && user.getGender() != null ? 
                    (user.getGender() == User.Gender.male ? "男" : "女") : "");
                row.createCell(colNum++).setCellValue(user != null && user.getBirthDate() != null ? 
                    user.getBirthDate().format(dateFormatter) : "");
                row.createCell(colNum++).setCellValue(group != null ? group.getName() : "");
                row.createCell(colNum++).setCellValue(registration.getShirtSize() != null ? 
                    registration.getShirtSize().name() : "");
                row.createCell(colNum++).setCellValue(registration.getEmergencyContact() != null ? 
                    registration.getEmergencyContact() : "");
                row.createCell(colNum++).setCellValue(registration.getEmergencyPhone() != null ? 
                    registration.getEmergencyPhone() : "");
                row.createCell(colNum++).setCellValue(registration.getMedicalHistory() != null ? 
                    registration.getMedicalHistory() : "");
                row.createCell(colNum++).setCellValue(registration.getRemark() != null ? 
                    registration.getRemark() : "");
                row.createCell(colNum++).setCellValue(getStatusText(registration.getStatus()));
                row.createCell(colNum++).setCellValue(registration.getReviewRemark() != null ? 
                    registration.getReviewRemark() : "");
                row.createCell(colNum++).setCellValue(registration.getCreatedAt() != null ? 
                    registration.getCreatedAt().format(datetimeFormatter) : "");
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 11) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }

    private String getStatusText(Registration.Status status) {
        if (status == null) return "";
        switch (status) {
            case pending: return "待审核";
            case approved: return "已通过";
            case rejected: return "已拒绝";
            case cancelled: return "已取消";
            default: return status.name();
        }
    }
}
