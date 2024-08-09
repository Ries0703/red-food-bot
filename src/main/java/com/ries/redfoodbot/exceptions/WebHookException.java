package com.ries.redfoodbot.exceptions;

public class WebHookException extends Exception {
    public WebHookException(String message) {
        super(message);
    }

    public WebHookException() {
    }
}
