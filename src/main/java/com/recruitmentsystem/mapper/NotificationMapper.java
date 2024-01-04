package com.recruitmentsystem.mapper;

import com.recruitmentsystem.dto.NotificationDto;
import com.recruitmentsystem.entity.Notifications;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationMapper {
    public NotificationDto entityToDto(Notifications notification) {
        return NotificationDto.builder()
                .content(notification.getContent())
                .timeAgo(calculateTimeAgo(notification.getCreateDate()))
                .build();
    }

    public String calculateTimeAgo(LocalDateTime createDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createDate, now);

        if (duration.toMinutes() < 1) {
            return "Vừa xong";
        } else if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return minutes + " phút trước";
        } else if (duration.toDays() < 1) {
            long hours = duration.toHours();
            return hours + " giờ trước";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return createDate.format(formatter);
        }
    }
}
