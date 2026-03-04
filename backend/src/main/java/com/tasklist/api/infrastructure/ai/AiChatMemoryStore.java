package com.tasklist.api.infrastructure.ai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiChatMemoryStore {

    private final int maxMessages;
    private final ConcurrentHashMap<String, ChatMemory> memories = new ConcurrentHashMap<>();

    public AiChatMemoryStore(@Value("${ai.assistant.memory.max-messages:20}") int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public ChatMemory getOrCreate(Object sessionId) {
        return memories.computeIfAbsent(sessionId.toString(),
                id -> MessageWindowChatMemory.withMaxMessages(maxMessages));
    }
}
