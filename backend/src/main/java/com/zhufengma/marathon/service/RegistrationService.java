package com.zhufengma.marathon.service;

import com.zhufengma.marathon.entity.Event;
import com.zhufengma.marathon.entity.EventGroup;
import com.zhufengma.marathon.entity.Registration;
import com.zhufengma.marathon.entity.User;
import com.zhufengma.marathon.repository.EventGroupRepository;
import com.zhufengma.marathon.repository.EventRepository;
import com.zhufengma.marathon.repository.RegistrationRepository;
import com.zhufengma.marathon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventGroupRepository eventGroupRepository;

    @Autowired
    private UserRepository userRepository;

    // 用户方法
    public List<Registration> getUserRegistrations(Long userId) {
        return registrationRepository.findByUserIdAndIsDeleted(userId, 0);
    }

    @Transactional
    public Registration createRegistration(Registration registration, Long userId) {
        // 检查用户是否存在
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查赛事是否存在
        Event event = eventRepository.findById(registration.getEventId())
            .orElseThrow(() -> new RuntimeException("赛事不存在"));

        // 检查赛事状态
        if (event.getStatus() != Event.Status.published) {
            throw new RuntimeException("赛事未发布，无法报名");
        }

        // 检查报名时间
        LocalDate today = LocalDate.now();
        if (today.isBefore(event.getRegistrationStartDate())) {
            throw new RuntimeException("报名尚未开始");
        }
        if (today.isAfter(event.getRegistrationEndDate())) {
            throw new RuntimeException("报名已结束");
        }

        // 检查组别是否存在
        EventGroup group = eventGroupRepository.findById(registration.getGroupId())
            .orElseThrow(() -> new RuntimeException("组别不存在"));

        if (!group.getEventId().equals(event.getId())) {
            throw new RuntimeException("组别不属于该赛事");
        }

        // 检查用户是否已经报名该赛事
        if (registrationRepository.existsByUserIdAndEventIdAndStatusInAndIsDeleted(
            userId, event.getId(), 
            List.of(Registration.Status.pending, Registration.Status.approved), 0)) {
            throw new RuntimeException("您已经报名过该赛事");
        }

        // 检查年龄限制
        int age = Period.between(user.getBirthDate(), today).getYears();
        if (group.getAgeMin() != null && age < group.getAgeMin()) {
            throw new RuntimeException("年龄不符合要求，最小年龄为 " + group.getAgeMin() + " 岁");
        }
        if (group.getAgeMax() != null && age > group.getAgeMax()) {
            throw new RuntimeException("年龄不符合要求，最大年龄为 " + group.getAgeMax() + " 岁");
        }

        // 检查性别限制
        if (group.getGenderLimit() != EventGroup.GenderLimit.all) {
            if (group.getGenderLimit() == EventGroup.GenderLimit.male && 
                user.getGender() != User.Gender.male) {
                throw new RuntimeException("该组别仅限男性报名");
            }
            if (group.getGenderLimit() == EventGroup.GenderLimit.female && 
                user.getGender() != User.Gender.female) {
                throw new RuntimeException("该组别仅限女性报名");
            }
        }

        // 检查名额是否已满
        Long registeredCount = registrationRepository.countByGroupIdAndStatusIn(
            group.getId(), List.of(Registration.Status.pending, Registration.Status.approved));
        if (registeredCount >= group.getMaxParticipants()) {
            throw new RuntimeException("该组别名额已满");
        }

        // 创建报名
        registration.setUserId(userId);
        registration.setStatus(Registration.Status.pending);
        
        // 使用用户信息填充紧急联系人（如果未提供）
        if (registration.getEmergencyContact() == null || registration.getEmergencyContact().isEmpty()) {
            registration.setEmergencyContact(user.getEmergencyContact());
            registration.setEmergencyPhone(user.getEmergencyPhone());
        }

        return registrationRepository.save(registration);
    }

    @Transactional
    public Registration cancelRegistration(Long registrationId, Long userId) {
        Registration registration = registrationRepository.findByIdAndIsDeleted(registrationId, 0)
            .orElseThrow(() -> new RuntimeException("报名不存在"));

        if (!registration.getUserId().equals(userId)) {
            throw new RuntimeException("无权取消该报名");
        }

        if (registration.getStatus() == Registration.Status.cancelled) {
            throw new RuntimeException("该报名已取消");
        }

        registration.setStatus(Registration.Status.cancelled);
        return registrationRepository.save(registration);
    }

    // 管理员方法
    public List<Registration> getEventRegistrations(Long eventId) {
        return registrationRepository.findByEventIdAndIsDeleted(eventId, 0);
    }

    public List<Registration> getEventRegistrationsByStatus(Long eventId, List<Registration.Status> statuses) {
        return registrationRepository.findByEventIdAndStatusInAndIsDeleted(eventId, statuses, 0);
    }

    public Optional<Registration> getRegistrationById(Long id) {
        return registrationRepository.findByIdAndIsDeleted(id, 0);
    }

    @Transactional
    public Registration reviewRegistration(Long registrationId, Registration.Status status, 
                                            String reviewRemark, Long adminId) {
        Registration registration = registrationRepository.findByIdAndIsDeleted(registrationId, 0)
            .orElseThrow(() -> new RuntimeException("报名不存在"));

        if (registration.getStatus() != Registration.Status.pending) {
            throw new RuntimeException("该报名已审核，无法重复审核");
        }

        if (status != Registration.Status.approved && status != Registration.Status.rejected) {
            throw new RuntimeException("审核状态无效");
        }

        registration.setStatus(status);
        registration.setReviewRemark(reviewRemark);
        registration.setReviewedBy(adminId);
        registration.setReviewedAt(LocalDateTime.now());

        return registrationRepository.save(registration);
    }

    @Transactional
    public List<Registration> batchReview(List<Long> registrationIds, Registration.Status status, 
                                           String reviewRemark, Long adminId) {
        List<Registration> registrations = registrationRepository.findByIdInAndIsDeleted(registrationIds, 0);
        List<Registration> updatedRegistrations = new ArrayList<>();

        for (Registration registration : registrations) {
            if (registration.getStatus() == Registration.Status.pending) {
                registration.setStatus(status);
                registration.setReviewRemark(reviewRemark);
                registration.setReviewedBy(adminId);
                registration.setReviewedAt(LocalDateTime.now());
                updatedRegistrations.add(registrationRepository.save(registration));
            }
        }

        return updatedRegistrations;
    }

    // 统计方法
    public Map<String, Object> getRegistrationStatistics(Long eventId) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取所有组别
        List<EventGroup> groups = eventGroupRepository.findByEventIdAndIsDeleted(eventId, 0);
        
        // 按组别统计报名人数
        List<Object[]> groupStats = registrationRepository.countByEventIdAndStatusInGroupByGroupId(
            eventId, List.of(Registration.Status.pending, Registration.Status.approved));
        
        Map<Long, Long> groupCounts = new HashMap<>();
        for (Object[] stat : groupStats) {
            groupCounts.put((Long) stat[0], (Long) stat[1]);
        }

        List<Map<String, Object>> groupStatistics = new ArrayList<>();
        for (EventGroup group : groups) {
            Map<String, Object> groupStat = new HashMap<>();
            groupStat.put("groupId", group.getId());
            groupStat.put("groupName", group.getName());
            groupStat.put("distance", group.getDistance());
            groupStat.put("maxParticipants", group.getMaxParticipants());
            groupStat.put("registeredCount", groupCounts.getOrDefault(group.getId(), 0L));
            groupStat.put("remainingCount", group.getMaxParticipants() - groupCounts.getOrDefault(group.getId(), 0L));
            groupStatistics.add(groupStat);
        }

        statistics.put("groupStatistics", groupStatistics);
        
        // 总统计
        long totalRegistered = groupCounts.values().stream().mapToLong(Long::longValue).sum();
        int totalCapacity = groups.stream().mapToInt(EventGroup::getMaxParticipants).sum();
        
        statistics.put("totalRegistered", totalRegistered);
        statistics.put("totalCapacity", totalCapacity);
        statistics.put("totalRemaining", totalCapacity - totalRegistered);

        return statistics;
    }
}
