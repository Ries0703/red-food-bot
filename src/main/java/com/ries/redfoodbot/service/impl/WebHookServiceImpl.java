package com.ries.redfoodbot.service.impl;

import com.ries.redfoodbot.service.WebHookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebHookServiceImpl implements WebHookService {

    @Value("${config.verify_token}")
    private String VERIFY_TOKEN;

    @Override
    public boolean verifyWebHookSubscription(Map<String, String> query) {
        String mode = query.get("hub.mode");
        String token = query.get("hub.verify_token");
        return "subscribe".equals(mode) && VERIFY_TOKEN.equals(token);
    }
}
