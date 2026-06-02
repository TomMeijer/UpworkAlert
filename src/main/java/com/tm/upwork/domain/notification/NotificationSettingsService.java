package com.tm.upwork.domain.notification;

import com.tm.upwork.domain.notification.entity.NotificationSettings;
import com.tm.upwork.domain.notification.model.NotificationSettingsDto;
import com.tm.upwork.domain.notification.model.UpdateNotificationSettingsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationSettingsService {

    private final NotificationSettingsRepository notificationSettingsRepository;
    private final NotificationSettingsMapper notificationSettingsMapper;

    public NotificationSettingsDto get() {
        return notificationSettingsRepository.findFirstByOrderByIdAsc()
                .map(notificationSettingsMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("No notification settings found."));
    }

    @Transactional
    public void update(UpdateNotificationSettingsRequest request) {
        NotificationSettings settings = notificationSettingsRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("No notification settings found."));
        settings.setEmailEnabled(request.isEmailEnabled());
        settings.setRecipientEmail(request.getRecipientEmail());
        notificationSettingsRepository.save(settings);
    }
}
