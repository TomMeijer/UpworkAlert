package com.tm.upwork.domain.chat.client;

import com.tm.upwork.domain.job.entity.Job;

public interface JobChatClient {

    String startChat(Job job);

    String sendMessage(Job job, String message);
}
