package com.ries.redfoodbot.controller;

import com.ries.redfoodbot.service.WebHookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.badRequest()
                                 .build();
        }
    }
}
