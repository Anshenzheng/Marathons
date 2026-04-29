package com.zhufengma.marathon.controller;

import com.zhufengma.marathon.dto.ApiResponse;
import com.zhufengma.marathon.entity.Announcement;
import com.zhufengma.marathon.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/announcements")
@CrossOrigin(origins = "*")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // 公开接口
    @GetMapping("/public/list")
    public ResponseEntity<ApiResponse<List<Announcement>>> getPublishedAnnouncements() {
        List<Announcement> announcements = announcementService.getPublishedAnnouncements();
        return ResponseEntity.ok(ApiResponse.success(announcements));
    }

    @GetMapping("/public/event/{eventId}")
    public ResponseEntity<ApiResponse<List<Announcement>>> getEventAnnouncements(@PathVariable Long eventId) {
        List<Announcement> announcements = announcementService.getEventAnnouncements(eventId);
        return ResponseEntity.ok(ApiResponse.success(announcements));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<Announcement>> getAnnouncementById(@PathVariable Long id) {
        Optional<Announcement> announcement = announcementService.getAnnouncementById(id);
        if (announcement.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(announcement.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // 管理员接口
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Announcement>>> getAllAnnouncements() {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(ApiResponse.success(announcements));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Announcement>> createAnnouncement(
            @RequestBody Announcement announcement,
            @RequestHeader("X-User-Id") Long adminId) {
        try {
            Announcement createdAnnouncement = announcementService.createAnnouncement(announcement, adminId);
            return ResponseEntity.ok(ApiResponse.success("公告创建成功", createdAnnouncement));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Announcement>> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody Announcement announcementDetails) {
        try {
            Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcementDetails);
            return ResponseEntity.ok(ApiResponse.success("公告更新成功", updatedAnnouncement));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Announcement>> publishAnnouncement(@PathVariable Long id) {
        try {
            Announcement announcement = announcementService.publishAnnouncement(id);
            return ResponseEntity.ok(ApiResponse.success("公告发布成功", announcement));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long id) {
        try {
            announcementService.deleteAnnouncement(id);
            return ResponseEntity.ok(ApiResponse.success("公告删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
