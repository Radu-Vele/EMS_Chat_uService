package com.chatus.controllers;

import com.chatus.dtos.MessageDto;
import com.chatus.exceptions.NoAdminOnlineException;
import com.chatus.services.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ChatController {
    private final ChatService chatService;

    /**
     * Called when a user wants to send a message to another user
     * @param messageDto
     */
    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDto messageDto) throws JsonProcessingException {
        this.chatService.processChatMessage(messageDto);
    }

    @GetMapping("/getActiveSessions")
    private ResponseEntity<List<String>> getAllActiveConnections() {
        return ResponseEntity.ok(this.chatService.getAllActiveConnections());
    }

    @GetMapping("/findAdminAvailable")
    private ResponseEntity<String> findAdminAvailable() throws NoAdminOnlineException {
        return ResponseEntity.ok(this.chatService.findAdminAvailable());
    }
}
