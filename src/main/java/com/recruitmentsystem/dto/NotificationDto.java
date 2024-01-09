package com.recruitmentsystem.dto;

import lombok.Builder;

@Builder
public record NotificationDto(
        Integer id,
        String content,
        boolean isRead,
        String timeAgo
) {
}