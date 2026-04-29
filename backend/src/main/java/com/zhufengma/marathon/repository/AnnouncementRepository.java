package com.zhufengma.marathon.repository;

import com.zhufengma.marathon.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByStatusAndIsDeleted(Announcement.Status status, Integer isDeleted);
    List<Announcement> findByEventIdAndIsDeleted(Long eventId, Integer isDeleted);
    List<Announcement> findByEventIdAndStatusAndIsDeleted(Long eventId, Announcement.Status status, Integer isDeleted);
    
    @Query("SELECT a FROM Announcement a WHERE a.isDeleted = 0 AND a.status = 'published' ORDER BY a.isTop DESC, a.createdAt DESC")
    List<Announcement> findPublishedOrderByTopAndDate();
}
