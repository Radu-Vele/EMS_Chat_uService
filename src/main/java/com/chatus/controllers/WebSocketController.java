package com.chatus.controllers;

import com.chatus.dtos.message.MessageDto;
import com.chatus.dtos.SeenNotificationDto;
import com.chatus.dtos.TypingNotificationDto;
import com.chatus.exceptions.NoAdminOnlineException;
import com.chatus.services.WebsocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class WebSocketController {
    private final WebsocketService websocketService;

    /**
     * Called when a user wants to send a message to another user
     * @param messageDto
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDto messageDto) throws JsonProcessingException {
        this.websocketService.processChatMessage(messageDto);
    }

    @MessageMapping("/seen")
    public void sendSeenNotification(@Payload SeenNotificationDto seenNotificationDto) throws JsonProcessingException {
        this.websocketService.sendSeenNotification(seenNotificationDto);
    }

    @MessageMapping("/startTyping")
    public void sendStartTypingNotification(@Payload TypingNotificationDto typingNotificationDto) throws JsonProcessingException {
        this.websocketService.sendStartTypingNotification(typingNotificationDto);
    }

    @MessageMapping("/stopTyping")
    public void sendStopTypingNotification(@Payload TypingNotificationDto typingNotificationDto) throws JsonProcessingException {
        this.websocketService.sendStopTypingNotification(typingNotificationDto);
    }

    @GetMapping("/getActiveSessions")
    private ResponseEntity<List<String>> getAllActiveConnections() {
        return ResponseEntity.ok(this.websocketService.getAllActiveConnections());
    }

    @GetMapping("/findAdminAvailable")
    private ResponseEntity<String> findAdminAvailable() throws NoAdminOnlineException {
        return ResponseEntity.ok(this.websocketService.findAdminAvailable());
    }
}
