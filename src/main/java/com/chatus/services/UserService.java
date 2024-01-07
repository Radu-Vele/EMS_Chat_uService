package com.chatus.services;

import com.chatus.dtos.UserChatDto;
import com.chatus.dtos.UserCompleteDto;
import com.chatus.dtos.UserCreateDto;
import com.chatus.entities.Chat;
import com.chatus.entities.ChatRoom;
import com.chatus.entities.GroupChat;
import com.chatus.entities.User;
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
        // TODO: verify if I need to call save (probably yes).
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
}
