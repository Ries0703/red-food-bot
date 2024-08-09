package com.ries.redfoodbot.service;

import java.util.Map;

public interface WebHookService {
    boolean verifyWebHookSubscription(Map<String, String> query);
    void handleWebHookEvent(Map<String, Object> body) throws Exception;
}
