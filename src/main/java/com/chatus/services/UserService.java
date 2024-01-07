package com.chatus.services;

import com.chatus.dtos.chat.ChatWithMessageCreateDto;
import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.dtos.message.MessageSaveInChatDto;
import com.chatus.dtos.user.UserChatDto;
import com.chatus.dtos.user.UserCompleteDto;
import com.chatus.dtos.user.UserCreateDto;
import com.chatus.entities.*;
import com.chatus.exceptions.DocumentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;
    private final ChatService chatService;
    public Optional<User> getUserByEmail(String userEmail) {
        Query query = new Query(Criteria.where("emailAddress").is(userEmail));
        return Optional.ofNullable(this.mongoTemplate
                .findOne(query, User.class));
    }

    public String create(UserCreateDto userCreateDto) {
        // TODO: maybe allow only admins to create admins
        return this.mongoTemplate
                .insert(this.modelMapper.map(userCreateDto, User.class))
                .getId();
    }

    public UserCompleteDto getById(String id) throws DocumentNotFoundException {
        User user = this.mongoTemplate.findById(id, User.class);
        if (user == null) {
            throw new DocumentNotFoundException();
        }
        return this.modelMapper
                .map(user, UserCompleteDto.class);
    }


    public void delete(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        this.mongoTemplate.findAndRemove(query, User.class);
    }

    public String edit(UserCompleteDto userCompleteDto) throws DocumentNotFoundException {
        Query query = new Query(Criteria.where("id").is(userCompleteDto.getId()));
        User previous = this.mongoTemplate.findOne(query, User.class);
        if (previous == null) {
            throw new DocumentNotFoundException();
        }
        previous.setStatus(userCompleteDto.getStatus());
        return this.mongoTemplate.save(previous).getId();
    }

    public void addChat(UserChatDto userChatDto) throws DocumentNotFoundException {
        User user = this.mongoTemplate.findById(userChatDto.getUserId(), User.class);
        if (user == null) {
            throw new DocumentNotFoundException();
        }
        Chat chat = this.chatService.getById(userChatDto.getChatId());
        user.getChats().add(chat);
        this.mongoTemplate.save(user);
    }

    public void removeChat(UserChatDto userChatDto) throws DocumentNotFoundException {
        User user = this.mongoTemplate.findById(userChatDto.getUserId(), User.class);
        if (user == null) {
            throw new DocumentNotFoundException();
        }
        Chat chat = this.chatService.getById(userChatDto.getChatId());
        user.getChats().add(chat);
    }

    public void addChatInternal(User currUser, ChatRoom savedChat) {
        currUser.getChats().add(savedChat);
        this.mongoTemplate.save(currUser);
    }

    public void removeChatInternal(User user, GroupChat groupChat) {
        user.getChats().remove(groupChat);
        this.mongoTemplate.save(user);
    }

    public String addNewChat(ChatWithMessageCreateDto newChatDto, String requesterEmailAddress) throws DocumentNotFoundException {
        User sender = this.getUserByEmail(requesterEmailAddress)
                .orElseThrow(DocumentNotFoundException::new);
        User receiver = this.getUserByEmail(newChatDto.getReceiverEmail())
                .orElseThrow(DocumentNotFoundException::new);
        // check if there is already a chat between the two
        for(ChatRoom chatRoom : sender.getChats()) {
            if (chatRoom instanceof Chat) {
                Chat chat = (Chat)chatRoom;
                if (chat.getEndpoint2().equals(receiver) || chat.getEndpoint1().equals(receiver)) {
                    System.out.println("Adding message to existing chat");
                    return this.chatService.addNewMessageInternal(chat, MessageSaveInChatDto.builder()
                            .body(newChatDto.getBody())
                            .senderEmail(requesterEmailAddress)
                            .timestamp(newChatDto.getTimestamp()));
                }
            }
        }

        return this.chatService.createInternal(sender, receiver, newChatDto);
    }
}
