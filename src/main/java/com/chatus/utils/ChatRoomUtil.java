package com.chatus.utils;

import com.chatus.entities.ChatRoom;
import com.chatus.entities.Message;
import org.springframework.stereotype.Component;

import static com.chatus.constants.DatabaseConstants.MAX_MESSAGES_IN_CHAT;

@Component
public final class ChatRoomUtil {
    public void addMessage(Message message, ChatRoom chatRoom) {
        if (chatRoom.getMessages().size() >= MAX_MESSAGES_IN_CHAT) {
            chatRoom.getMessages().remove(0); // pop oldest element
        }
        chatRoom.getMessages().add(message);
    }
}
