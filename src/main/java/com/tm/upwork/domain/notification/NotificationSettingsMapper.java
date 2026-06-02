package com.tm.upwork.domain.notification;

import com.tm.upwork.domain.notification.entity.NotificationSettings;
import com.tm.upwork.domain.notification.model.NotificationSettingsDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingsMapper {

    public NotificationSettingsDto toDto(NotificationSettings entity) {
        return NotificationSettingsDto.builder()
                .emailEnabled(entity.isEmailEnabled())
                .recipientEmail(entity.getRecipientEmail())
                .build();
    }
}
