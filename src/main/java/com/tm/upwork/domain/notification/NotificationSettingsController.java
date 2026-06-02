package com.tm.upwork.domain.notification;

import com.tm.upwork.domain.notification.model.NotificationSettingsDto;
import com.tm.upwork.domain.notification.model.UpdateNotificationSettingsRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification-settings")
@RequiredArgsConstructor
public class NotificationSettingsController {

    private final NotificationSettingsService notificationSettingsService;

    @GetMapping
    public NotificationSettingsDto get() {
        return notificationSettingsService.get();
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateNotificationSettingsRequest request) {
        notificationSettingsService.update(request);
    }
}
