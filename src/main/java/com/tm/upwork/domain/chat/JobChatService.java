package com.tm.upwork.domain.chat;

import com.tm.upwork.domain.chat.client.JobChatClient;
import com.tm.upwork.domain.chat.entity.Chat;
import com.tm.upwork.domain.chat.entity.ChatMessage;
import com.tm.upwork.domain.chat.entity.ChatRole;
import com.tm.upwork.domain.job.entity.Job;
import com.tm.upwork.domain.job.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobChatService {

    private final JobRepository jobRepository;
    private final JobChatClient jobChatClient;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;

    @JobLock(key = "#jobId")
    @Transactional
    public ChatMessageDto sendMessage(int jobId, String message) {
        Job job = findJob(jobId);
        if (job.getChat() == null) {
            String externalChatId = jobChatClient.startChat(job);
            job.setChat(new Chat(externalChatId));
            job = jobRepository.save(job);
        }
        saveMessage(job.getChat(), ChatRole.USER, message);
        String response = jobChatClient.sendMessage(job, message);
        var assistantMessage = saveMessage(job.getChat(), ChatRole.ASSISTANT, response);
        return chatMessageMapper.toDto(assistantMessage);
    }

    private Job findJob(int id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No job found with id: " + id));
    }

    private ChatMessage saveMessage(Chat chat, ChatRole role, String content) {
        var chatMessage = new ChatMessage();
        chatMessage.setChat(chat);
        chatMessage.setRole(role);
        chatMessage.setTime(LocalDateTime.now());
        chatMessage.setContent(content);
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getChatHistory(int jobId) {
        Job job = findJob(jobId);
        if (job.getChat() == null) {
            return List.of();
        }
        List<ChatMessage> messages = chatMessageRepository.findByChatIdOrderByTimeAsc(job.getChat().getId());
        return chatMessageMapper.toDtoList(messages);
    }
}
