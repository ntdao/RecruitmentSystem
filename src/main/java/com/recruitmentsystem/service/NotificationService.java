package com.recruitmentsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitmentsystem.dto.NotificationDto;
import com.recruitmentsystem.entity.Account;
import com.recruitmentsystem.entity.Notifications;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    public int countAllNotification(Integer id) {
        return notificationRepository.countByAccountId(id);
    }

    public int countNewNotification(Integer id) {
        return notificationRepository.countNewByAccountId(id);
    }

    public List<NotificationDto> getAccountNotification(Integer id) {
        return notificationRepository.getAccountNotification(id)
                .stream()
                .map(n -> objectMapper.convertValue(n, NotificationDto.class))
                .toList();
    }

    public List<NotificationDto> getAccountNotificationPaging(Integer accountId, PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());

        Page<Notifications> pagedResult = notificationRepository.getAccountNotificationPaging(accountId, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(n -> objectMapper.convertValue(n, NotificationDto.class))
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public void addNotification(String content, Account account) {
        Notifications notification = Notifications.builder()
                .content(content)
                .isRead(false)
                .account(account)
                .build();
        notificationRepository.save(notification);
    }
}
