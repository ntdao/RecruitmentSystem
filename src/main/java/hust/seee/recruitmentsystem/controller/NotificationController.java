package hust.seee.recruitmentsystem.controller;

import hust.seee.recruitmentsystem.dto.NotificationDTO;
import hust.seee.recruitmentsystem.pagination.PageDto;
import hust.seee.recruitmentsystem.service.NotificationService;
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
    public List<NotificationDTO> getAccountNotification(@PathVariable("accountId") Integer accountId) {
        return notificationService.getAccountNotification(accountId);
    }

    @PostMapping("/{accountId}/page")
    public List<NotificationDTO> getAccountNotificationPaging(@PathVariable("accountId") Integer accountId,
                                                              @RequestBody PageDto page) {
        return notificationService.getAccountNotificationPaging(accountId, page);
    }
}
