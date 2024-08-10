package com.ries.redfoodbot.service.impl;

import com.ries.redfoodbot.config.FacebookConfig;
import com.ries.redfoodbot.service.FacebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class FacebookServiceImpl implements FacebookService {

    private static final Logger logger = LoggerFactory.getLogger(FacebookServiceImpl.class);

    private final WebClient webClient;
    private final FacebookConfig facebookConfig;

    public FacebookServiceImpl(WebClient.Builder webClientBuilder, FacebookConfig facebookConfig) {
        this.facebookConfig = facebookConfig;
        this.webClient = webClientBuilder.baseUrl(String.format("%s/%s/%s", this.facebookConfig.getApiBaseUrl(), this.facebookConfig.getApiVersion(), this.facebookConfig.getPageId()))
                                         .build();
    }

    @Override
    public void sendResponseToUser(String senderPsId, Map<String, Object> response) {
        try {
            webClient.post()
                     .uri(uriBuilder -> uriBuilder.path("/messages")
                                                  .queryParam("access_token", this.facebookConfig.getPageAccessToken())
                                                  .build())
                     .body(BodyInserters.fromValue(Map.of("recipient", Map.of("id", senderPsId), "messaging_type", "RESPONSE", "message", response)))
                     .retrieve()
                     .bodyToMono(Void.class)
                     .block(); // Blocking call to wait for the response
            logger.info("Message sent!");
        } catch (WebClientResponseException e) {
            logger.error("Unable to send message: {}", e.getResponseBodyAsString());
            logger.error("Status code: {}", e.getStatusCode());
        } catch (Exception e) {
            logger.error("Unable to send message: {}", (Object) e.getStackTrace());
        }
    }

    // Method to send typing action
    @Override
    public void sendTypingAction(String senderPsId, String action) {
        try {
            // Construct the request body
            Map<String, Object> requestBody = Map.of(
                    "recipient", Map.of("id", senderPsId),
                    "sender_action", action
            );

            logger.info("Sending {} action to sender {}", action, senderPsId);

            // Send the request
            webClient.post()
                     .uri(uriBuilder -> uriBuilder.path("/messages")
                                                  .queryParam("access_token", facebookConfig.getPageAccessToken())
                                                  .build())
                     .body(BodyInserters.fromValue(requestBody))
                     .retrieve()
                     .bodyToMono(Void.class)
                     .block(); // Blocking call to wait for the response

            logger.info("Sent {} action!", action);
        } catch (WebClientResponseException e) {
            logger.error("Unable to send message: {}", e.getResponseBodyAsString());
            logger.error("Status code: {}", e.getStatusCode());
        } catch (Exception e) {
            logger.error("Unable to send {} action: {}", action, e.getMessage());
        }
    }
}

