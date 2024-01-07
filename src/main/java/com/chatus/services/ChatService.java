package com.chatus.services;

import com.chatus.entities.Chat;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    public Chat getById(String chatId) {
        return new Chat();
    }
}
