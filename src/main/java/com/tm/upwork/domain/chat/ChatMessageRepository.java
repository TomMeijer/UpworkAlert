package com.tm.upwork.domain.chat;

import com.tm.upwork.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByChatIdOrderByTimeAsc(int chatId);
}
