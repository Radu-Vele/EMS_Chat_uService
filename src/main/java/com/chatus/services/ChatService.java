package com.chatus.services;

import com.chatus.data.User;
import com.chatus.data.UserRole;
import com.chatus.dtos.MessageDto;
import com.chatus.dtos.SeenNotificationDto;
import com.chatus.dtos.TypingNotificationDto;
import com.chatus.exceptions.NoAdminOnlineException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getAllActiveConnections() {
        System.out.println(this.simpUserRegistry.getUserCount());
        return this.simpUserRegistry.getUsers().stream()
                .map(simpUser -> {
                    return Objects.requireNonNull(simpUser.getPrincipal()).toString();
                })
                .toList();
    }

    /**
     * @return the username of an online admin, randomly selected
     */
    public String findAdminAvailable() throws NoAdminOnlineException {
        Random random = new Random();
        List<String> adminList = this.simpUserRegistry.getUsers().stream()
                .filter(simpUser -> {
                    User user = (User) simpUser.getPrincipal();
                    if (user == null) {
                        return false;
                    }
                    return user.getRole().equals(UserRole.ADMIN);
                })
                .map(SimpUser::getName)
                .toList();
        if (adminList.isEmpty()) {
            throw new NoAdminOnlineException();
        }
        return adminList.get(random.nextInt(adminList.size()));
    }

    public void processChatMessage(MessageDto messageDto) throws JsonProcessingException {
        simpMessagingTemplate.convertAndSendToUser(
                messageDto.getReceiverEmailAddress(),
                "/messages",
                objectMapper.writeValueAsString(messageDto)
        );
    }

    public void sendSeenNotification(SeenNotificationDto seenNotificationDto) throws JsonProcessingException {
        simpMessagingTemplate.convertAndSend(
                "/seen/".concat(String.valueOf(seenNotificationDto.getMessageTimestamp())),
                objectMapper.writeValueAsString(seenNotificationDto)
        );
    }

    public void sendStopTypingNotification(TypingNotificationDto typingNotificationDto) throws JsonProcessingException {
        simpMessagingTemplate.convertAndSend(
                "/stopTyping/".concat(String.valueOf(typingNotificationDto.getSenderEmailAddress())),
                objectMapper.writeValueAsString(typingNotificationDto)
        );
    }

    public void sendStartTypingNotification(TypingNotificationDto typingNotificationDto) throws JsonProcessingException {
        simpMessagingTemplate.convertAndSend(
                "/startTyping/".concat(String.valueOf(typingNotificationDto.getSenderEmailAddress())),
                objectMapper.writeValueAsString(typingNotificationDto)
        );
    }
}
