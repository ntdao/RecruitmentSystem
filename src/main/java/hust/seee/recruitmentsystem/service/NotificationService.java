package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.dto.NotificationDTO;
import hust.seee.recruitmentsystem.entity.Account;
import hust.seee.recruitmentsystem.entity.Notifications;
import hust.seee.recruitmentsystem.mapper.NotificationMapper;
import hust.seee.recruitmentsystem.pagination.PageDto;
import hust.seee.recruitmentsystem.repository.NotificationRepository;
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
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;

    public int countAllNotification(Integer id) {
        return notificationRepository.countByAccountId(id);
    }

    public int countNewNotification(Integer id) {
        return notificationRepository.countNewByAccountId(id);
    }

    public List<NotificationDTO> getAccountNotification(Integer id) {
        return notificationRepository.getAccountNotification(id)
                .stream()
                .map(notificationMapper::entityToDto)
                .toList();
    }

    public List<NotificationDTO> getAccountNotificationPaging(Integer accountId, PageDto pageDto) {
        Pageable paging = PageRequest.of(
                pageDto.getPageNo(),
                pageDto.getPageSize(),
                Sort.Direction.fromString(pageDto.getSortDir()),
                pageDto.getSortBy());

        Page<Notifications> pagedResult = notificationRepository.getAccountNotificationPaging(accountId, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent()
                    .stream()
                    .map(notificationMapper::entityToDto)
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
