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

    @Value("${config.facebook_api_base_url}")
    private String facebookApiBaseUrl;

    @Value("${config.facebook_api_version}")
    private String facebookApiVersion;

    @Value("${config.facebook_page_id}")
    private String facebookPageId;

    @Value("${config.facebook_message_path}")
    private String facebookMessagePath;

    @Value("${config.page_access_token}")
    private String pageAccessToken;

    public FacebookServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(String.format("%s/%s/%s",
                                                 facebookApiBaseUrl,
                                                 facebookApiVersion,
                                                 facebookPageId))
                                         .build();
    }

    @Override
    public void sendResponseToUser(String senderPsId, Map<String, Object> response) {
        try {
            webClient.post()
                     .uri(uriBuilder -> uriBuilder.path(facebookMessagePath)
                                                  .queryParam("access_token", pageAccessToken)
                                                  .build())
                     .body(BodyInserters.fromValue(Map.of(
                             "recipient", Map.of("id", senderPsId),
                             "messaging_type", "RESPONSE",
                             "message", response
                     )))
                     .retrieve()
                     .bodyToMono(Void.class)
                     .block(); // Blocking call to wait for the response
            logger.info("Message sent!");
        } catch (WebClientResponseException e) {
            logger.error("Unable to send message: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unable to send message: {}", (Object) e.getStackTrace());
        }
    }
}

