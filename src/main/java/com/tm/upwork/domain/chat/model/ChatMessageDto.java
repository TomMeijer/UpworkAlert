package com.tm.upwork.domain.chat.model;

import com.tm.upwork.domain.chat.entity.ChatRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class ChatMessageDto {
    private final int id;
    private final ChatRole role;
    private final LocalDateTime time;
    private final String content;
}
