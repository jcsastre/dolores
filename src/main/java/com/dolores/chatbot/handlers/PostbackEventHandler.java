package com.dolores.chatbot.handlers;

import com.dolores.chatbot.payloads.PostbackPayload;
import com.github.messenger4j.webhook.event.PostbackEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostbackEventHandler {

    @Async
    public void handle(PostbackEvent postbackEvent) {

        postbackEvent.payload().ifPresent(this::processPayload);
    }

    private void processPayload(String payload) {

        log.debug("payload: {}", payload);

        switch (PostbackPayload.valueOf(payload)) {

            case get_started:
                //TODO
                break;

            default:
                //TODO
        }
    }
}
