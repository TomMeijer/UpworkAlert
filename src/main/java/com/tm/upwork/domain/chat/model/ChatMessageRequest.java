package com.tm.upwork.domain.chat.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChatMessageRequest {
    @NotBlank(message = "Message cannot be blank")
    private String message;
}
