package com.ries.redfoodbot.controller;

import com.ries.redfoodbot.service.WebHookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController {

    private static final Logger logger = LoggerFactory.getLogger(WebHookController.class);
    private final WebHookService webHookService;

    @GetMapping
    public ResponseEntity<String> getWebHook(@RequestParam Map<String, String> query) {
        try {
            boolean isValid = webHookService.verifyWebHookSubscription(query);
            if (isValid) {
                logger.info("WEBHOOK_VERIFIED");
                return ResponseEntity.ok(query.get("hub.challenge"));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .build();
            }
        } catch (Exception exception) {
            logger.error("Error verifying webhook subscription:{}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .build();
        }
    }


    @PostMapping
    public ResponseEntity<String> postWebHook(@RequestBody Map<String, Object> body) {
        try {
            if ("page".equals(body.get("object"))) {
                webHookService.handleWebHookEvent(body);
                return ResponseEntity.ok("EVENT_RECEIVED");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .build();
            }
        } catch (Exception e) {
            logger.error("error handling webhook event {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .build();
        }
    }
}
