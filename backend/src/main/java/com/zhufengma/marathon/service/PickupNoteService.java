package com.zhufengma.marathon.service;

import com.zhufengma.marathon.entity.PickupNote;
import com.zhufengma.marathon.repository.PickupNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PickupNoteService {

    @Autowired
    private PickupNoteRepository pickupNoteRepository;

    // 公开方法
    public Optional<PickupNote> getPublishedPickupNoteByEventId(Long eventId) {
        return pickupNoteRepository.findByEventIdAndStatusAndIsDeletedOrderByCreatedAtDesc(
            eventId, PickupNote.Status.published, 0);
    }

    public List<PickupNote> getPickupNotesByEventId(Long eventId) {
        return pickupNoteRepository.findByEventIdAndStatusAndIsDeleted(
            eventId, PickupNote.Status.published, 0);
    }

    // 管理员方法
    public List<PickupNote> getAllPickupNotesByEventId(Long eventId) {
        return pickupNoteRepository.findByEventIdAndIsDeleted(eventId, 0);
    }

    public Optional<PickupNote> getPickupNoteById(Long id) {
        return pickupNoteRepository.findById(id);
    }

    @Transactional
    public PickupNote createPickupNote(PickupNote pickupNote, Long adminId) {
        pickupNote.setCreatedBy(adminId);
        if (pickupNote.getStatus() == null) {
            pickupNote.setStatus(PickupNote.Status.draft);
        }
        return pickupNoteRepository.save(pickupNote);
    }

    @Transactional
    public PickupNote updatePickupNote(Long id, PickupNote pickupNoteDetails) {
        PickupNote pickupNote = pickupNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("领物须知不存在"));

        pickupNote.setTitle(pickupNoteDetails.getTitle());
        pickupNote.setContent(pickupNoteDetails.getContent());
        pickupNote.setPickupDate(pickupNoteDetails.getPickupDate());
        pickupNote.setPickupLocation(pickupNoteDetails.getPickupLocation());

        return pickupNoteRepository.save(pickupNote);
    }

    @Transactional
    public PickupNote publishPickupNote(Long id) {
        PickupNote pickupNote = pickupNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("领物须知不存在"));
        
        pickupNote.setStatus(PickupNote.Status.published);
        return pickupNoteRepository.save(pickupNote);
    }

    @Transactional
    public void deletePickupNote(Long id) {
        PickupNote pickupNote = pickupNoteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("领物须知不存在"));
        
        pickupNote.setIsDeleted(1);
        pickupNoteRepository.save(pickupNote);
    }
}
