package com.zhufengma.marathon.controller;

import com.zhufengma.marathon.dto.ApiResponse;
import com.zhufengma.marathon.entity.PickupNote;
import com.zhufengma.marathon.service.PickupNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pickup-notes")
@CrossOrigin(origins = "*")
public class PickupNoteController {

    @Autowired
    private PickupNoteService pickupNoteService;

    // 公开接口
    @GetMapping("/public/event/{eventId}")
    public ResponseEntity<ApiResponse<PickupNote>> getPublishedPickupNote(@PathVariable Long eventId) {
        Optional<PickupNote> pickupNote = pickupNoteService.getPublishedPickupNoteByEventId(eventId);
        if (pickupNote.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(pickupNote.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/public/event/{eventId}/list")
    public ResponseEntity<ApiResponse<List<PickupNote>>> getPickupNotesByEvent(@PathVariable Long eventId) {
        List<PickupNote> pickupNotes = pickupNoteService.getPickupNotesByEventId(eventId);
        return ResponseEntity.ok(ApiResponse.success(pickupNotes));
    }

    // 管理员接口
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PickupNote>>> getAllPickupNotesByEvent(@PathVariable Long eventId) {
        List<PickupNote> pickupNotes = pickupNoteService.getAllPickupNotesByEventId(eventId);
        return ResponseEntity.ok(ApiResponse.success(pickupNotes));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PickupNote>> getPickupNoteById(@PathVariable Long id) {
        Optional<PickupNote> pickupNote = pickupNoteService.getPickupNoteById(id);
        if (pickupNote.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(pickupNote.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PickupNote>> createPickupNote(
            @RequestBody PickupNote pickupNote,
            @RequestHeader("X-User-Id") Long adminId) {
        try {
            PickupNote createdPickupNote = pickupNoteService.createPickupNote(pickupNote, adminId);
            return ResponseEntity.ok(ApiResponse.success("领物须知创建成功", createdPickupNote));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PickupNote>> updatePickupNote(
            @PathVariable Long id,
            @RequestBody PickupNote pickupNoteDetails) {
        try {
            PickupNote updatedPickupNote = pickupNoteService.updatePickupNote(id, pickupNoteDetails);
            return ResponseEntity.ok(ApiResponse.success("领物须知更新成功", updatedPickupNote));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PickupNote>> publishPickupNote(@PathVariable Long id) {
        try {
            PickupNote pickupNote = pickupNoteService.publishPickupNote(id);
            return ResponseEntity.ok(ApiResponse.success("领物须知发布成功", pickupNote));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePickupNote(@PathVariable Long id) {
        try {
            pickupNoteService.deletePickupNote(id);
            return ResponseEntity.ok(ApiResponse.success("领物须知删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
