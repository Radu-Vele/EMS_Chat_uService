package com.chatus.controllers;

import com.chatus.dtos.MessageDto;
import com.chatus.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDto messageDto) {
        messagingTemplate.convertAndSendToUser(
                messageDto.getReceiverEmailAddress(),
                "/queue/messages",
                messageDto.getMessageBody()
        );
    }

    @GetMapping("/getActiveSessions")
    private ResponseEntity<List<String>> getAllActiveConnections() {
        return ResponseEntity.ok(this.chatService.getAllActiveConnections());
    }
}
