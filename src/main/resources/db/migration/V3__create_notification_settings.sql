CREATE TABLE notification_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email_enabled BOOLEAN NOT NULL,
    recipient_email VARCHAR(255)
);

INSERT INTO notification_settings (email_enabled) VALUES (false);
