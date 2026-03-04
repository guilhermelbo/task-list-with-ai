package com.tasklist.api.infrastructure.ai;

import com.tasklist.api.application.assistant.TaskAssistant;
import com.tasklist.api.application.tool.TaskTools;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    @ConditionalOnProperty(name = "ai.provider", havingValue = "openrouter")
    public ChatLanguageModel openRouterChatModel(
            @Value("${ai.openrouter.base-url}") String baseUrl,
            @Value("${ai.openrouter.api-key}") String apiKey,
            @Value("${ai.openrouter.model-name}") String modelName,
            @Value("${ai.openrouter.temperature:0.3}") Double temperature,
            @Value("${ai.openrouter.max-tokens:2048}") Integer maxTokens) {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "ai.provider", havingValue = "ollama")
    public ChatLanguageModel ollamaChatModel(
            @Value("${ai.ollama.base-url}") String baseUrl,
            @Value("${ai.ollama.model-name}") String modelName,
            @Value("${ai.ollama.temperature:0.3}") Double temperature,
            @Value("${ai.ollama.max-tokens:2048}") Integer maxTokens) {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .temperature(temperature)
                .numPredict(maxTokens)
                .timeout(Duration.ofSeconds(120))
                .build();
    }

    @Bean
    @ConditionalOnExpression("'${ai.provider:disabled}' == 'openrouter' || '${ai.provider:disabled}' == 'ollama'")
    public TaskAssistant taskAssistant(ChatLanguageModel model, TaskTools tools, AiChatMemoryStore store) {
        return AiServices.builder(TaskAssistant.class)
                .chatLanguageModel(model)
                .tools(tools)
                .chatMemoryProvider(store::getOrCreate)
                .build();
    }
}
