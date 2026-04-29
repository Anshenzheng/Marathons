package com.zhufengma.marathon.repository;

import com.zhufengma.marathon.entity.Regulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Long> {
    List<Regulation> findByEventIdAndIsDeleted(Long eventId, Integer isDeleted);
    List<Regulation> findByEventIdAndStatusAndIsDeleted(Long eventId, Regulation.Status status, Integer isDeleted);
    Optional<Regulation> findByEventIdAndStatusAndIsDeletedOrderByVersionDesc(Long eventId, Regulation.Status status, Integer isDeleted);
}
