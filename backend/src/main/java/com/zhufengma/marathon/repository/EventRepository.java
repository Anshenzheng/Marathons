package com.zhufengma.marathon.repository;

import com.zhufengma.marathon.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatusAndIsDeleted(Event.Status status, Integer isDeleted);
    List<Event> findByStatusInAndIsDeleted(List<Event.Status> statuses, Integer isDeleted);
    
    @Query("SELECT e FROM Event e WHERE e.isDeleted = 0 AND e.registrationStartDate <= :today AND e.registrationEndDate >= :today")
    List<Event> findActiveRegistrationEvents(LocalDate today);
    
    @Query("SELECT e FROM Event e WHERE e.isDeleted = 0 AND e.eventDate >= :today ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents(LocalDate today);
}
