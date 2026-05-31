package com.tm.upwork.domain.chat;

import com.tm.upwork.domain.chat.model.ChatMessageDto;
import com.tm.upwork.domain.chat.model.ChatMessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/{jobId}/chat")
@RequiredArgsConstructor
public class JobChatController {

    private final JobChatService jobChatService;
    private final ChatMessageMapper chatMessageMapper;

    @PostMapping("/message")
    public ChatMessageDto sendMessage(@PathVariable int jobId, @RequestBody @Valid ChatMessageRequest request) {
        return chatMessageMapper.toDto(jobChatService.sendMessage(jobId, request.getMessage()));
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> getChatHistory(@PathVariable int jobId) {
        return chatMessageMapper.toDtoList(jobChatService.getChatHistory(jobId));
    }
}
