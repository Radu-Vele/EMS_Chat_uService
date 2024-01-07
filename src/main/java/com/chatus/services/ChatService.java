package com.chatus.services;

import com.chatus.dtos.chat.ChatWithMessageCreateDto;
import com.chatus.dtos.message.MessageSaveInChatDto;
import com.chatus.entities.Chat;
import com.chatus.entities.Message;
import com.chatus.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private MessageService messageService;
    private MongoTemplate mongoTemplate;
    public Chat getById(String chatId) {
        return new Chat();
    }

    public String addNewMessageInternal(Chat chat, MessageSaveInChatDto.MessageSaveInChatDtoBuilder timestamp) {
        return "";
    }

    public String createInternal(User endpoint1, User endpoint2, ChatWithMessageCreateDto chatWithMessageCreateDto) {
        // create chat
        // create message
        // add message to chat
        return "";
    }
}
