package com.tasklist.api.application.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface TaskAssistant {

    @SystemMessage(fromResource = "ai-system-prompt.txt")
    String chat(@MemoryId String sessionId, @UserMessage String userMessage);
}
