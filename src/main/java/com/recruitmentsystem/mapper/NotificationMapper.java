package com.recruitmentsystem.mapper;

import com.recruitmentsystem.dto.NotificationDto;
import com.recruitmentsystem.entity.Notifications;
import com.recruitmentsystem.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationMapper {
    public NotificationDto entityToDto(Notifications notification) {
        return NotificationDto.builder()
                .content(notification.getContent())
                .timeAgo(Utils.calculateTimeAgo(notification.getCreateDate()))
                .build();
    }
}
