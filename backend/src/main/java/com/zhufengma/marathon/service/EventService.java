package com.zhufengma.marathon.service;

import com.zhufengma.marathon.entity.Event;
import com.zhufengma.marathon.entity.EventGroup;
import com.zhufengma.marathon.repository.EventGroupRepository;
import com.zhufengma.marathon.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventGroupRepository eventGroupRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findByStatusInAndIsDeleted(
            List.of(Event.Status.published, Event.Status.offline), 0
        );
    }

    public List<Event> getActiveEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findActiveRegistrationEvents(today);
    }

    public List<Event> getUpcomingEvents() {
        LocalDate today = LocalDate.now();
        return eventRepository.findUpcomingEvents(today);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<EventGroup> getEventGroups(Long eventId) {
        return eventGroupRepository.findByEventIdAndIsDeleted(eventId, 0);
    }

    public Optional<EventGroup> getEventGroupById(Long groupId) {
        return eventGroupRepository.findById(groupId);
    }

    // 管理员方法
    @Transactional
    public Event createEvent(Event event, Long adminId) {
        event.setCreatedBy(adminId);
        event.setStatus(Event.Status.draft);
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("赛事不存在"));

        event.setName(eventDetails.getName());
        event.setDescription(eventDetails.getDescription());
        event.setEventDate(eventDetails.getEventDate());
        event.setEventTime(eventDetails.getEventTime());
        event.setLocation(eventDetails.getLocation());
        event.setRegistrationStartDate(eventDetails.getRegistrationStartDate());
        event.setRegistrationEndDate(eventDetails.getRegistrationEndDate());

        return eventRepository.save(event);
    }

    @Transactional
    public Event publishEvent(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("赛事不存在"));
        
        // 检查是否有至少一个组别
        List<EventGroup> groups = eventGroupRepository.findByEventIdAndIsDeleted(id, 0);
        if (groups.isEmpty()) {
            throw new RuntimeException("发布赛事前请先添加至少一个组别");
        }
        
        event.setStatus(Event.Status.published);
        return eventRepository.save(event);
    }

    @Transactional
    public Event offlineEvent(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("赛事不存在"));
        
        event.setStatus(Event.Status.offline);
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("赛事不存在"));
        
        event.setIsDeleted(1);
        eventRepository.save(event);
    }

    // 组别管理
    @Transactional
    public EventGroup createEventGroup(EventGroup group, Long eventId) {
        group.setEventId(eventId);
        return eventGroupRepository.save(group);
    }

    @Transactional
    public EventGroup updateEventGroup(Long groupId, EventGroup groupDetails) {
        EventGroup group = eventGroupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("组别不存在"));

        group.setName(groupDetails.getName());
        group.setDistance(groupDetails.getDistance());
        group.setMaxParticipants(groupDetails.getMaxParticipants());
        group.setRegistrationFee(groupDetails.getRegistrationFee());
        group.setAgeMin(groupDetails.getAgeMin());
        group.setAgeMax(groupDetails.getAgeMax());
        group.setGenderLimit(groupDetails.getGenderLimit());

        return eventGroupRepository.save(group);
    }

    @Transactional
    public void deleteEventGroup(Long groupId) {
        EventGroup group = eventGroupRepository.findById(groupId)
            .orElseThrow(() -> new RuntimeException("组别不存在"));
        
        group.setIsDeleted(1);
        eventGroupRepository.save(group);
    }
}
