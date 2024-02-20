package com.recruitmentsystem.controller;

import com.recruitmentsystem.dto.NotificationDto;
import com.recruitmentsystem.pagination.PageDto;
import com.recruitmentsystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{accountId}/new")
    public Integer countNewNotification(@PathVariable("accountId") Integer accountId) {
        return notificationService.countNewNotification(accountId);
    }

    @GetMapping("/{accountId}")
    public List<NotificationDto> getAccountNotification(@PathVariable("accountId") Integer accountId) {
        return notificationService.getAccountNotification(accountId);
    }

    @PostMapping("/{accountId}/page")
    public List<NotificationDto> getAccountNotificationPaging(@PathVariable("accountId") Integer accountId,
                                                              @RequestBody PageDto page) {
        return notificationService.getAccountNotificationPaging(accountId, page);
    }
}
