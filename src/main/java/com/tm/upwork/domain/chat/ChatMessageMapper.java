package com.tm.upwork.domain.chat;

import com.tm.upwork.domain.chat.entity.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMessageMapper {

    public List<ChatMessageDto> toDtoList(List<ChatMessage> entities) {
        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    public ChatMessageDto toDto(ChatMessage entity) {
        return ChatMessageDto.builder()
                .id(entity.getId())
                .role(entity.getRole())
                .time(entity.getTime())
                .content(entity.getContent())
                .build();
    }
}
