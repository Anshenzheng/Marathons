package com.zhufengma.marathon.repository;

import com.zhufengma.marathon.entity.EventGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventGroupRepository extends JpaRepository<EventGroup, Long> {
    List<EventGroup> findByEventIdAndIsDeleted(Long eventId, Integer isDeleted);
    List<EventGroup> findByEventIdInAndIsDeleted(List<Long> eventIds, Integer isDeleted);
}
