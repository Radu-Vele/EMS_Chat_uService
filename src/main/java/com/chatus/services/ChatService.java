package com.chatus.services;

import com.chatus.dtos.chat.ChatCompleteDto;
import com.chatus.dtos.chat.ChatWithMessageCreateDto;
import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.dtos.message.MessageSaveDto;
import com.chatus.dtos.message.MessageSaveInChatDto;
import com.chatus.entities.Chat;
import com.chatus.entities.Message;
import com.chatus.entities.User;
import com.chatus.exceptions.ActionNotAllowedException;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.utils.ChatRoomUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageService messageService;
    private final MongoTemplate mongoTemplate;
    private final ChatRoomUtil chatRoomUtil;
    private final ModelMapper modelMapper;

    private Chat checkMembershipAndReturnChat(String chatId, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        Chat chat = this.getByIdInternal(chatId);
        if (!chat.getEndpoint1().getEmailAddress().equals(requesterEmailAddress)
                && !chat.getEndpoint2().getEmailAddress().equals(requesterEmailAddress)) {
            throw new ActionNotAllowedException();
        }
        return chat;
    }
    public ChatCompleteDto getById(String chatId, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        Chat chat = this.checkMembershipAndReturnChat(chatId, requesterEmailAddress);
        return ChatCompleteDto.builder()
                .id(chat.getId())
                .endpoint1Email(chat.getEndpoint1().getEmailAddress())
                .endpoint2Email(chat.getEndpoint2().getEmailAddress())
                .messageCompleteDtoList(chat.getMessages()
                        .stream()
                        .map(m ->
                                this.modelMapper.map(m, MessageCompleteDto.class))
                        .toList())
                .build();
    }

    public String addNewMessageInternal(Chat chat, MessageSaveInChatDto messageDto) {
        Message savedMessage = this.messageService.saveInternal(Message.builder()
                        .body(messageDto.getBody())
                        .timestamp(messageDto.getTimestamp())
                        .chatRoom(chat.getId())
                        .senderEmail(messageDto.getSenderEmail()).build()
        );
        this.chatRoomUtil.addMessage(savedMessage, chat);
        this.mongoTemplate.save(chat);
        return savedMessage.getId();
    }

    // Endpoint1 needs to be the sender of the message (initiator of conversation)
    public Chat createInternal(User endpoint1, User endpoint2, ChatWithMessageCreateDto chatWithMessageCreateDto) {
        // create chat
        Chat savedChat = this.mongoTemplate.insert(Chat.builder()
                .endpoint1(endpoint1)
                .endpoint2(endpoint2)
                .build());
        // create message
        Message savedMessage = this.messageService
                .saveInternal(Message.builder()
                        .senderEmail(endpoint1.getEmailAddress())
                        .body(chatWithMessageCreateDto.getBody())
                        .timestamp(chatWithMessageCreateDto.getTimestamp())
                        .chatRoom(savedChat.getId())
                        .build());
        // add message to chat
        this.chatRoomUtil.addMessage(savedMessage, savedChat);
        return this.mongoTemplate.save(savedChat);
    }

    public Chat getByIdInternal(String chatId) throws DocumentNotFoundException {
        Chat chat = this.mongoTemplate.findById(chatId, Chat.class);
        if (chat == null) {
            throw new DocumentNotFoundException();
        }
        return chat;
    }

    public void deleteById(String chatId, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        Chat chat = this.checkMembershipAndReturnChat(chatId, requesterEmailAddress);
        this.mongoTemplate.remove(chat);
    }

    public String addNewMessage(MessageSaveInChatDto messageSaveInChatDto, String requesterEmailAddress) throws DocumentNotFoundException, ActionNotAllowedException {
        Chat chat = this.checkMembershipAndReturnChat(messageSaveInChatDto.getChatRoomId(), requesterEmailAddress);
        if (!messageSaveInChatDto.getSenderEmail().equals(requesterEmailAddress)) {
            throw new ActionNotAllowedException();
        }
        return this.addNewMessageInternal(chat, messageSaveInChatDto);
    }
}
