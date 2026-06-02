package com.tm.upwork.domain.notification.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotificationSettingsRequest {
    private boolean emailEnabled;
    @Email
    private String recipientEmail;
}
