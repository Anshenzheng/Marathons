package com.zhufengma.marathon.controller;

import com.zhufengma.marathon.dto.ApiResponse;
import com.zhufengma.marathon.entity.Regulation;
import com.zhufengma.marathon.service.RegulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/regulations")
@CrossOrigin(origins = "*")
public class RegulationController {

    @Autowired
    private RegulationService regulationService;

    // 公开接口
    @GetMapping("/public/event/{eventId}")
    public ResponseEntity<ApiResponse<Regulation>> getPublishedRegulation(@PathVariable Long eventId) {
        Optional<Regulation> regulation = regulationService.getPublishedRegulationByEventId(eventId);
        if (regulation.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(regulation.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/public/event/{eventId}/list")
    public ResponseEntity<ApiResponse<List<Regulation>>> getRegulationsByEvent(@PathVariable Long eventId) {
        List<Regulation> regulations = regulationService.getRegulationsByEventId(eventId);
        return ResponseEntity.ok(ApiResponse.success(regulations));
    }

    // 管理员接口
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Regulation>>> getAllRegulationsByEvent(@PathVariable Long eventId) {
        List<Regulation> regulations = regulationService.getAllRegulationsByEventId(eventId);
        return ResponseEntity.ok(ApiResponse.success(regulations));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Regulation>> getRegulationById(@PathVariable Long id) {
        Optional<Regulation> regulation = regulationService.getRegulationById(id);
        if (regulation.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(regulation.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Regulation>> createRegulation(
            @RequestBody Regulation regulation,
            @RequestHeader("X-User-Id") Long adminId) {
        try {
            Regulation createdRegulation = regulationService.createRegulation(regulation, adminId);
            return ResponseEntity.ok(ApiResponse.success("规程创建成功", createdRegulation));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Regulation>> updateRegulation(
            @PathVariable Long id,
            @RequestBody Regulation regulationDetails) {
        try {
            Regulation updatedRegulation = regulationService.updateRegulation(id, regulationDetails);
            return ResponseEntity.ok(ApiResponse.success("规程更新成功", updatedRegulation));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Regulation>> publishRegulation(@PathVariable Long id) {
        try {
            Regulation regulation = regulationService.publishRegulation(id);
            return ResponseEntity.ok(ApiResponse.success("规程发布成功", regulation));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRegulation(@PathVariable Long id) {
        try {
            regulationService.deleteRegulation(id);
            return ResponseEntity.ok(ApiResponse.success("规程删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
