package com.zhufengma.marathon.controller;

import com.zhufengma.marathon.dto.ApiResponse;
import com.zhufengma.marathon.entity.Event;
import com.zhufengma.marathon.entity.EventGroup;
import com.zhufengma.marathon.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    // 公开接口
    @GetMapping("/public/list")
    public ResponseEntity<ApiResponse<List<Event>>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/public/active")
    public ResponseEntity<ApiResponse<List<Event>>> getActiveEvents() {
        List<Event> events = eventService.getActiveEvents();
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/public/upcoming")
    public ResponseEntity<ApiResponse<List<Event>>> getUpcomingEvents() {
        List<Event> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<Event>> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);
        if (event.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(event.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/public/{eventId}/groups")
    public ResponseEntity<ApiResponse<List<EventGroup>>> getEventGroups(@PathVariable Long eventId) {
        List<EventGroup> groups = eventService.getEventGroups(eventId);
        return ResponseEntity.ok(ApiResponse.success(groups));
    }

    // 管理员接口
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Event>> createEvent(@RequestBody Event event, 
                                                           @RequestHeader("X-User-Id") Long adminId) {
        try {
            Event createdEvent = eventService.createEvent(event, adminId);
            return ResponseEntity.ok(ApiResponse.success("赛事创建成功", createdEvent));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Event>> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(ApiResponse.success("赛事更新成功", updatedEvent));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Event>> publishEvent(@PathVariable Long id) {
        try {
            Event event = eventService.publishEvent(id);
            return ResponseEntity.ok(ApiResponse.success("赛事发布成功", event));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/offline")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Event>> offlineEvent(@PathVariable Long id) {
        try {
            Event event = eventService.offlineEvent(id);
            return ResponseEntity.ok(ApiResponse.success("赛事下架成功", event));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok(ApiResponse.success("赛事删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 组别管理
    @PostMapping("/{eventId}/groups")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventGroup>> createEventGroup(@PathVariable Long eventId, 
                                                                      @RequestBody EventGroup group) {
        try {
            EventGroup createdGroup = eventService.createEventGroup(group, eventId);
            return ResponseEntity.ok(ApiResponse.success("组别创建成功", createdGroup));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/groups/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventGroup>> updateEventGroup(@PathVariable Long groupId, 
                                                                      @RequestBody EventGroup groupDetails) {
        try {
            EventGroup updatedGroup = eventService.updateEventGroup(groupId, groupDetails);
            return ResponseEntity.ok(ApiResponse.success("组别更新成功", updatedGroup));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/groups/{groupId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEventGroup(@PathVariable Long groupId) {
        try {
            eventService.deleteEventGroup(groupId);
            return ResponseEntity.ok(ApiResponse.success("组别删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
