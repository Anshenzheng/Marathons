package com.zhufengma.marathon.repository;

import com.zhufengma.marathon.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<Registration> findByEventIdAndIsDeleted(Long eventId, Integer isDeleted);
    List<Registration> findByEventIdAndStatusInAndIsDeleted(Long eventId, List<Registration.Status> statuses, Integer isDeleted);
    List<Registration> findByGroupIdAndIsDeleted(Long groupId, Integer isDeleted);
    List<Registration> findByIdInAndIsDeleted(List<Long> ids, Integer isDeleted);
    
    Optional<Registration> findByIdAndIsDeleted(Long id, Integer isDeleted);
    Optional<Registration> findByUserIdAndEventIdAndIsDeleted(Long userId, Long eventId, Integer isDeleted);
    
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.groupId = :groupId AND r.status IN :statuses AND r.isDeleted = 0")
    Long countByGroupIdAndStatusIn(@Param("groupId") Long groupId, @Param("statuses") List<Registration.Status> statuses);
    
    @Query("SELECT r.groupId, COUNT(r) FROM Registration r WHERE r.eventId = :eventId AND r.status IN :statuses AND r.isDeleted = 0 GROUP BY r.groupId")
    List<Object[]> countByEventIdAndStatusInGroupByGroupId(@Param("eventId") Long eventId, @Param("statuses") List<Registration.Status> statuses);
    
    boolean existsByUserIdAndEventIdAndStatusInAndIsDeleted(Long userId, Long eventId, List<Registration.Status> statuses, Integer isDeleted);
}
