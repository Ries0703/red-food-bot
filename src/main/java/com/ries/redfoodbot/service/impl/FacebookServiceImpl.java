package com.ries.redfoodbot.service.impl;

import com.ries.redfoodbot.service.FacebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class FacebookServiceImpl implements FacebookService {

    private static final Logger logger = LoggerFactory.getLogger(FacebookServiceImpl.class);

    private final WebClient webClient;

    @Value("${config.page_access_token}")
    private String pageAccessToken;

    @Value("${config.facebook_messenger_uri}")
    private String facebookMessageUri;


    public FacebookServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public void sendResponseToUser(String senderPsId, Map<String, Object> response) {
        try {
            webClient.post()
                     .uri(uriBuilder -> uriBuilder.path(facebookMessageUri)
                                                  .queryParam("access_token", pageAccessToken)
                                                  .build())
                     .body(BodyInserters.fromValue(Map.of("recipient", Map.of("id", senderPsId), "messaging_type", "RESPONSE", "message", response)))
                     .retrieve()
                     .bodyToMono(Void.class)
                     .block(); // Blocking call to wait for the response
            System.out.println("Message sent!");
        } catch (WebClientResponseException e) {
            logger.error("Unable to send message: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unable to send message: {}", e.getMessage());
        }
    }
}
