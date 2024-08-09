package com.ries.redfoodbot.service.impl;

import com.google.gson.annotations.JsonAdapter;
import com.ries.redfoodbot.service.FacebookService;
import com.ries.redfoodbot.service.WebHookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebHookServiceImpl implements WebHookService {


    private static final Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);
    private final FacebookService facebookService;
    @Value("${facebook.verify-token}")
    private String verifyToken;


    @Override
    public boolean verifyWebHookSubscription(Map<String, String> query) {
        String mode = query.get("hub.mode");
        String token = query.get("hub.verify_token");
        return "subscribe".equals(mode) && verifyToken.equals(token);
    }

    @Override
    public void handleWebHookEvent(Map<String, Object> body) throws Exception {
        List<Map<String, Object>> entries = (List<Map<String, Object>>) body.get("entry");
        for (Map<String, Object> entry : entries) {
            // Get the first messaging event in the entry
            List<Map<String, Object>> messaging = (List<Map<String, Object>>) entry.get("messaging");
            if (messaging != null && !messaging.isEmpty()) {
                Map<String, Object> webhookEvent = messaging.get(0);
                System.out.println(webhookEvent);

                // Get the sender PSID
                String senderPsId = (String) ((Map<String, Object>) webhookEvent.get("sender")).get("id");
                logger.info("Sender PSID: {}", senderPsId);

                if (webhookEvent.containsKey("message")) {
                    // Construct the response message
                    Map<String, Object> responseMessage = Map.of("text", "Thank you for your message!");
                    logger.info(responseMessage.toString());
                    // Call the Facebook service to send the response
                    facebookService.sendResponseToUser(senderPsId, responseMessage);
                } else {
                    System.out.println("Received non-message event: " + webhookEvent);
                }
            }
        }
    }

}
