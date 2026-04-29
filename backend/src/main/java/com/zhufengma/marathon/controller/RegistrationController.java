package com.zhufengma.marathon.controller;

import com.zhufengma.marathon.dto.ApiResponse;
import com.zhufengma.marathon.entity.Registration;
import com.zhufengma.marathon.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    // 用户接口
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Registration>>> getMyRegistrations(
            @RequestHeader("X-User-Id") Long userId) {
        List<Registration> registrations = registrationService.getUserRegistrations(userId);
        return ResponseEntity.ok(ApiResponse.success(registrations));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Registration>> createRegistration(
            @RequestBody Registration registration,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            Registration createdRegistration = registrationService.createRegistration(registration, userId);
            return ResponseEntity.ok(ApiResponse.success("报名成功", createdRegistration));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Registration>> cancelRegistration(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            Registration registration = registrationService.cancelRegistration(id, userId);
            return ResponseEntity.ok(ApiResponse.success("取消报名成功", registration));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 管理员接口
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Registration>>> getEventRegistrations(
            @PathVariable Long eventId) {
        List<Registration> registrations = registrationService.getEventRegistrations(eventId);
        return ResponseEntity.ok(ApiResponse.success(registrations));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Registration>> getRegistrationById(@PathVariable Long id) {
        Optional<Registration> registration = registrationService.getRegistrationById(id);
        if (registration.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(registration.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Registration>> reviewRegistration(
            @PathVariable Long id,
            @RequestBody Map<String, Object> reviewRequest,
            @RequestHeader("X-User-Id") Long adminId) {
        try {
            String statusStr = (String) reviewRequest.get("status");
            Registration.Status status = Registration.Status.valueOf(statusStr);
            String reviewRemark = (String) reviewRequest.get("reviewRemark");

            Registration registration = registrationService.reviewRegistration(id, status, reviewRemark, adminId);
            return ResponseEntity.ok(ApiResponse.success("审核成功", registration));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/batch-review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Registration>>> batchReview(
            @RequestBody Map<String, Object> batchRequest,
            @RequestHeader("X-User-Id") Long adminId) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> registrationIds = (List<Long>) batchRequest.get("registrationIds");
            String statusStr = (String) batchRequest.get("status");
            Registration.Status status = Registration.Status.valueOf(statusStr);
            String reviewRemark = (String) batchRequest.get("reviewRemark");

            List<Registration> registrations = registrationService.batchReview(
                registrationIds, status, reviewRemark, adminId);
            return ResponseEntity.ok(ApiResponse.success("批量审核成功", registrations));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/statistics/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRegistrationStatistics(
            @PathVariable Long eventId) {
        Map<String, Object> statistics = registrationService.getRegistrationStatistics(eventId);
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
}
