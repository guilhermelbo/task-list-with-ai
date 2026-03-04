package com.tasklist.api.presentation.rest;

import com.tasklist.api.application.assistant.TaskAssistant;
import com.tasklist.api.presentation.rest.dto.ChatRequest;
import com.tasklist.api.presentation.rest.dto.ChatResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@ConditionalOnExpression("'${ai.provider:disabled}' == 'openrouter' || '${ai.provider:disabled}' == 'ollama'")
public class AiChatController {

    private final TaskAssistant taskAssistant;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String sessionId = (request.sessionId() != null && !request.sessionId().isBlank())
                ? request.sessionId()
                : UUID.randomUUID().toString();

        String response = taskAssistant.chat(sessionId, request.message());
        return ResponseEntity.ok(new ChatResponse(response, sessionId));
    }
}
