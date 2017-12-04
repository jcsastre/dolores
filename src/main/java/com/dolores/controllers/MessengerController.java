package com.dolores.controllers;

import com.dolores.chatbot.handlers.PostbackEventHandler;
import com.dolores.configuration.MessengerConfiguration;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static com.github.messenger4j.Messenger.*;
import static java.util.Optional.of;

@Slf4j
@RestController
@RequestMapping("/messenger")
public class MessengerController {

    private final Messenger messenger;
    private final PostbackEventHandler postbackEventHandler;

    public MessengerController(
        MessengerConfiguration messengerConfiguration,
        PostbackEventHandler postbackEventHandler
    ) {
        log.debug(
            "Initializing MessengerReceiveClient - appSecret: {} | verifyToken: {}",
            messengerConfiguration.getSecret(),
            messengerConfiguration.getWebhookVerificationToken()
        );

        this.messenger =
            Messenger.create(
                messengerConfiguration.getPageAccessToken(),
                messengerConfiguration.getSecret(),
                messengerConfiguration.getWebhookVerificationToken()
            );

        this.postbackEventHandler = postbackEventHandler;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(
        @RequestBody final String payload,
        @RequestHeader(SIGNATURE_HEADER_NAME) final String signature
    ) {
        log.info("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);

        try {
            this.messenger.onReceiveEvents(
                payload,
                of(signature),
                event -> {
                    final String senderId = event.senderId();
                    final Instant timestamp = event.timestamp();

                    if (event.isPostbackEvent()) {
                        postbackEventHandler.handle(event.asPostbackEvent());
                    } else if (event.isTextMessageEvent()) {
                        final TextMessageEvent textMessageEvent = event.asTextMessageEvent();
                        final String messageId = textMessageEvent.messageId();
                        final String text = textMessageEvent.text();

                        log.info("Received text message from '{}' at '{}' with content: {} (mid: {})",
                            senderId, timestamp, text, messageId);
                    }
                }
            );

            log.debug("Processed callback payload successfully");

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            log.info("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> verifyWebhook(
        @RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
        @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken,
        @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge
    ) {
        log.debug(
            "Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}",
            mode, verifyToken, challenge
        );

        try {
            messenger.verifyWebhook(mode, verifyToken);
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            log.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
