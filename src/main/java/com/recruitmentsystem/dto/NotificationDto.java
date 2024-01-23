package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record NotificationDto(
        Integer notificationId,
        String content,
        boolean isRead,
        String timeAgo
) {
}