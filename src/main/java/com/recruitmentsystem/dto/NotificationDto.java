package com.recruitmentsystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDto(
        Integer id,
        String content,
        boolean isRead,
        String timeAgo
) {
}