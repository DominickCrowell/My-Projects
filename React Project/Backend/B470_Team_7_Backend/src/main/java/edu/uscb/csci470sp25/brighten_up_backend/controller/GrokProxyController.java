package edu.uscb.csci470sp25.brighten_up_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/xai")
public class GrokProxyController {

    private static final Logger logger = LoggerFactory.getLogger(GrokProxyController.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${xai.api.key}")
    private String apiKey;

    @Value("${xai.api.base-url}")
    private String baseUrl;

    public GrokProxyController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.x.ai").build();
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping(value = "/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> proxyChatCompletions(@RequestBody String requestBody) {
        logger.info("📡 Proxying chat completion request to xAI API");

        try {
            // Parse the request body
            JsonNode requestJson = objectMapper.readTree(requestBody);

            // Extract the personality field (default to "mindful" if not provided)
            String personality = requestJson.has("personality") ? requestJson.get("personality").asText() : "mindful";

            // Define system prompt and temperature based on personality
            String systemPrompt;
            double temperature;
            switch (personality) {
	            case "mindful":
	                systemPrompt = "You are the Mental Health Desk, a supportive AI. Provide positive, empathetic, and mental health-focused responses. Encourage self-care, mindfulness, and resilience. Keep responses uplifting, and safe. Avoid medical advice or sensitive topics unless prompted appropriately.";
	                temperature = 1.0;
	                break;
	            case "roleModel":
	                systemPrompt = "You are the Mental Health Desk, a supportive AI acting as a wise role model and ideal figure. Provide direct, honest, insightful, and wise responses. Offer practical advice with a firm but caring tone, focusing on resilience and personal growth.  Avoid assuming gender. Keep responses concise and avoid medical advice or sensitive topics unless prompted appropriately.";
	                temperature = 0.9;
	                break;
	            case "drillSergeant":
	                systemPrompt = "You are the Mental Health Desk, a supportive AI acting as an psycological personal trainer. Provide motivational and no-nonsense responses, affirmations, without cussing. Encourage the user stop complaining by offering a better use of their time, acknowledge ways to take action and overcome challenges with. Use a gentle but commanding tone, keep responses concise, and avoid medical advice or sensitive topics unless prompted appropriately.";
	                temperature = 1.2;
	                break;
	            default:
                    logger.warn("Invalid personality value: {}. Defaulting to 'mindful'.", personality);
                    systemPrompt = "You are the Mental Health Desk, a supportive AI created by xAI. Provide positive, empathetic, and mental health-focused responses. Encourage self-care, mindfulness, and resilience. Keep responses concise, uplifting, and safe. Avoid medical advice or sensitive topics unless prompted appropriately.";
                    temperature = 0.7;
            }

            // Extract the messages array from the request
            ArrayNode messages = (ArrayNode) requestJson.get("messages");
            if (messages == null) {
                throw new IllegalArgumentException("Messages field is required in the request body.");
            }

            // Create a new messages array with the system prompt prepended
            ArrayNode updatedMessages = objectMapper.createArrayNode();
            ObjectNode systemMessage = objectMapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            updatedMessages.add(systemMessage);
            updatedMessages.addAll(messages);

            // Construct the updated request body
            ObjectNode updatedRequestBody = objectMapper.createObjectNode();
            updatedRequestBody.put("model", "grok-3-mini-beta");
            updatedRequestBody.set("messages", updatedMessages);
            updatedRequestBody.put("max_tokens", 1000);
            updatedRequestBody.put("temperature", temperature);

            // Convert the updated request body back to a string
            String modifiedRequestBody = objectMapper.writeValueAsString(updatedRequestBody);

            // Proxy the request to the xAI API
            return webClient
                    .post()
                    .uri(baseUrl + "/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(modifiedRequestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> {
                        logger.info("✅ Successfully received response from xAI API");
                        Map<String, Object> successResponse = new HashMap<>();
                        successResponse.put("status", HttpStatus.OK.value());
                        successResponse.put("message", "Success");
                        successResponse.put("data", response);
                        return ResponseEntity.ok(successResponse);
                    })
                    .onErrorResume(WebClientResponseException.class, e -> {
                        logger.error("❌ Failed to call xAI API: Status {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("status", e.getStatusCode().value());
                        errorResponse.put("error", e.getStatusText());
                        errorResponse.put("message", e.getResponseBodyAsString());
                        return Mono.just(ResponseEntity.status(e.getStatusCode()).body(errorResponse));
                    })
                    .onErrorResume(Exception.class, e -> {
                        logger.error("❌ Unexpected error while proxying to xAI API: {}", e.getMessage());
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                        errorResponse.put("error", "Internal Server Error");
                        errorResponse.put("message", "Failed to call xAI API: " + e.getMessage());
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                    });
        } catch (Exception e) {
            logger.error("❌ Error processing request: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", "Invalid request body: " + e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
        }
    }
}