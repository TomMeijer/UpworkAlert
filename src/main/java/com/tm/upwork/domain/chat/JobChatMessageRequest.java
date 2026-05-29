package com.tm.upwork.domain.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JobChatMessageRequest {
    @NotBlank(message = "Message cannot be blank")
    private String message;
}
