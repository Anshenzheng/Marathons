package com.zhufengma.marathon.service;

import com.zhufengma.marathon.entity.Regulation;
import com.zhufengma.marathon.repository.RegulationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegulationService {

    @Autowired
    private RegulationRepository regulationRepository;

    // 公开方法
    public Optional<Regulation> getPublishedRegulationByEventId(Long eventId) {
        return regulationRepository.findByEventIdAndStatusAndIsDeletedOrderByVersionDesc(
            eventId, Regulation.Status.published, 0);
    }

    public List<Regulation> getRegulationsByEventId(Long eventId) {
        return regulationRepository.findByEventIdAndStatusAndIsDeleted(
            eventId, Regulation.Status.published, 0);
    }

    // 管理员方法
    public List<Regulation> getAllRegulationsByEventId(Long eventId) {
        return regulationRepository.findByEventIdAndIsDeleted(eventId, 0);
    }

    public Optional<Regulation> getRegulationById(Long id) {
        return regulationRepository.findById(id);
    }

    @Transactional
    public Regulation createRegulation(Regulation regulation, Long adminId) {
        regulation.setCreatedBy(adminId);
        regulation.setVersion(1);
        if (regulation.getStatus() == null) {
            regulation.setStatus(Regulation.Status.draft);
        }
        return regulationRepository.save(regulation);
    }

    @Transactional
    public Regulation updateRegulation(Long id, Regulation regulationDetails) {
        Regulation regulation = regulationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("规程不存在"));

        regulation.setTitle(regulationDetails.getTitle());
        regulation.setContent(regulationDetails.getContent());
        regulation.setVersion(regulation.getVersion() + 1);

        return regulationRepository.save(regulation);
    }

    @Transactional
    public Regulation publishRegulation(Long id) {
        Regulation regulation = regulationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("规程不存在"));
        
        regulation.setStatus(Regulation.Status.published);
        return regulationRepository.save(regulation);
    }

    @Transactional
    public void deleteRegulation(Long id) {
        Regulation regulation = regulationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("规程不存在"));
        
        regulation.setIsDeleted(1);
        regulationRepository.save(regulation);
    }
}
