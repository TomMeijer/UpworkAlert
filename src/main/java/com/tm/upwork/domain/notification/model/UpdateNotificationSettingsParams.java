package com.tm.upwork.domain.notification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateNotificationSettingsParams {
    private final boolean emailEnabled;
    private final String recipientEmail;
}
