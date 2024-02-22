package hust.seee.recruitmentsystem.mapper;

import hust.seee.recruitmentsystem.dto.NotificationDTO;
import hust.seee.recruitmentsystem.entity.Notifications;
import hust.seee.recruitmentsystem.utils.Utils;
import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {
    public NotificationDTO entityToDto(Notifications notification) {
        return NotificationDTO.builder()
                .notificationId(notification.getNotificationId())
                .content(notification.getContent())
                .timeAgo(Utils.calculateTimeAgo(notification.getCreateDate()))
                .build();
    }
}
