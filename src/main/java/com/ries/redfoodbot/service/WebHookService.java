package com.ries.redfoodbot.service;

import java.util.Map;

public interface WebHookService {
    boolean verifyWebHookSubscription(Map<String, String> query);
}
