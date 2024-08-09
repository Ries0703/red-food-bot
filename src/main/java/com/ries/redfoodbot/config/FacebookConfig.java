package com.ries.redfoodbot.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "facebook")
@Getter
public class FacebookConfig {
    private final String apiBaseUrl;
    private final String apiVersion;
    private final String pageId;
    private final String messagePath;
    private final String pageAccessToken;

    public FacebookConfig(String apiBaseUrl, String apiVersion, String pageId, String messagePath,
                          String pageAccessToken) {
        this.apiBaseUrl = apiBaseUrl;
        this.apiVersion = apiVersion;
        this.pageId = pageId;
        this.messagePath = messagePath;
        this.pageAccessToken = pageAccessToken;
    }
}
