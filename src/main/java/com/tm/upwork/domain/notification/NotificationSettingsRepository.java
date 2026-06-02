package com.tm.upwork.domain.notification;

import com.tm.upwork.domain.notification.entity.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Integer> {
    Optional<NotificationSettings> findFirstByOrderByIdAsc();
}
