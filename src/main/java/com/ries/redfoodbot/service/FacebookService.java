package com.ries.redfoodbot.service;

import java.util.Map;

public interface FacebookService {
    void sendResponseToUser(String senderPsId, Map<String, Object> response);
}
