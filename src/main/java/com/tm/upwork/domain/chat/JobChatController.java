package com.tm.upwork.domain.chat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/{jobId}/chat")
@RequiredArgsConstructor
public class JobChatController {

    private final JobChatService jobChatService;

    @PostMapping("/message")
    public ChatMessageDto sendMessage(@PathVariable int jobId, @Valid @RequestBody JobChatMessageRequest request) {
        return jobChatService.sendMessage(jobId, request.getMessage());
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> getChatHistory(@PathVariable int jobId) {
        return jobChatService.getChatHistory(jobId);
    }
}
