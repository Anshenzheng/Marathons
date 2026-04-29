package com.zhufengma.marathon.repository;

import com.zhufengma.marathon.entity.PickupNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickupNoteRepository extends JpaRepository<PickupNote, Long> {
    List<PickupNote> findByEventIdAndIsDeleted(Long eventId, Integer isDeleted);
    List<PickupNote> findByEventIdAndStatusAndIsDeleted(Long eventId, PickupNote.Status status, Integer isDeleted);
    Optional<PickupNote> findByEventIdAndStatusAndIsDeletedOrderByCreatedAtDesc(Long eventId, PickupNote.Status status, Integer isDeleted);
}
