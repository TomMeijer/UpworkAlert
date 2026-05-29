package com.tm.upwork.domain.chat.client.openai;

import com.openai.client.OpenAIClient;
import com.openai.models.conversations.ConversationCreateParams;
import com.openai.models.responses.*;
import com.tm.upwork.domain.chat.client.JobChatClient;
import com.tm.upwork.domain.job.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Primary
public class OpenAiJobChatClient implements JobChatClient {

    private static final String CHAT_MODEL = "gpt-5.5";
    private static final String CHAT_INSTRUCTIONS = """
            You are an assistant that needs to help a freelancer apply for the following Upwork job:
            
            Job title:
            %s
            
            Job description:
            %s
            """;

    private final OpenAIClient client;
    private final String vectorStoreId;

    @Autowired
    public OpenAiJobChatClient(OpenAIClient client,
                               @Value("${openai.vector-store-id}") String vectorStoreId) {
        this.client = client;
        this.vectorStoreId = vectorStoreId;
    }

    @Override
    public String startChat(Job job) {
        var params = ConversationCreateParams.builder()
                .addItem(EasyInputMessage.builder()
                        .role(EasyInputMessage.Role.DEVELOPER)
                        .content(CHAT_INSTRUCTIONS.formatted(job.getTitle(), job.getDescription()))
                        .build())
                .build();
        return client.conversations().create(params).id();
    }

    @Override
    public String sendMessage(Job job, String message) {
        var paramsBuilder = ResponseCreateParams.builder()
                .model(CHAT_MODEL)
                .input(message);
        if (job.getChat() != null) {
            paramsBuilder.conversation(job.getChat().getExternalId());
        }
        if (vectorStoreId != null && !vectorStoreId.isBlank()) {
            paramsBuilder.addTool(FileSearchTool.builder()
                    .addVectorStoreId(vectorStoreId)
                    .build());
        }
        var params = paramsBuilder.build();
        Response response = client.responses().create(params);
        return getTextContent(response);
    }

    private String getTextContent(Response response) {
        return response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(ResponseOutputText::text)
                .collect(Collectors.joining());
    }
}
