package com.chatus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public List<String> getAllActiveConnections() {
        System.out.println(this.simpUserRegistry.getUserCount());
        return this.simpUserRegistry.getUsers().stream()
                .map(SimpUser::getName)
                .toList();
    }

    public String findAdminForClient() {
        return "";
    }
}
