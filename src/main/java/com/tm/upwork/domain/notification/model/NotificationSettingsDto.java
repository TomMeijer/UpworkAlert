package com.tm.upwork.domain.notification.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class NotificationSettingsDto {
    private final boolean emailEnabled;
    private final String recipientEmail;
}
