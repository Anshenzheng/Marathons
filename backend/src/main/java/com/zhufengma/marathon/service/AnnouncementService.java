package com.zhufengma.marathon.service;

import com.zhufengma.marathon.entity.Announcement;
import com.zhufengma.marathon.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    // 公开方法
    public List<Announcement> getPublishedAnnouncements() {
        return announcementRepository.findPublishedOrderByTopAndDate();
    }

    public List<Announcement> getEventAnnouncements(Long eventId) {
        return announcementRepository.findByEventIdAndStatusAndIsDeleted(
            eventId, Announcement.Status.published, 0);
    }

    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }

    // 管理员方法
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    @Transactional
    public Announcement createAnnouncement(Announcement announcement, Long adminId) {
        announcement.setCreatedBy(adminId);
        if (announcement.getStatus() == null) {
            announcement.setStatus(Announcement.Status.draft);
        }
        return announcementRepository.save(announcement);
    }

    @Transactional
    public Announcement updateAnnouncement(Long id, Announcement announcementDetails) {
        Announcement announcement = announcementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("公告不存在"));

        announcement.setTitle(announcementDetails.getTitle());
        announcement.setContent(announcementDetails.getContent());
        announcement.setEventId(announcementDetails.getEventId());
        announcement.setType(announcementDetails.getType());
        announcement.setIsTop(announcementDetails.getIsTop());

        return announcementRepository.save(announcement);
    }

    @Transactional
    public Announcement publishAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("公告不存在"));
        
        announcement.setStatus(Announcement.Status.published);
        return announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("公告不存在"));
        
        announcement.setIsDeleted(1);
        announcementRepository.save(announcement);
    }
}
