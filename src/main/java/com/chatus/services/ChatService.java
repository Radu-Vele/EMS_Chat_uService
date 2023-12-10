package com.chatus.services;

import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public List<String> getAllActiveConnections() {
        System.out.println(this.simpUserRegistry.getUserCount());
        return this.simpUserRegistry.getUsers().stream()
                .map(simpUser -> {
                    return Objects.requireNonNull(simpUser.getPrincipal()).toString();
                })
                .toList();
    }
}
