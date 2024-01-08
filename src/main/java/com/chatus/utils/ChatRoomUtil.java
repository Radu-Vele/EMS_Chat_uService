package com.chatus.utils;

import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.entities.ChatRoom;
import com.chatus.entities.Message;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.services.MessageService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.chatus.constants.DatabaseConstants.MAX_MESSAGES_IN_CHAT;

@Component
public final class ChatRoomUtil {
    public void addMessage(Message message, ChatRoom chatRoom) {
        if (chatRoom.getMessages().size() >= MAX_MESSAGES_IN_CHAT) {
            chatRoom.getMessages().remove(0); // pop oldest element
        }
        chatRoom.getMessages().add(message);
    }

    public void editMessageEmbeddedOrNot(MessageCompleteDto messageCompleteDto, ChatRoom chatRoom, MessageService messageService) throws DocumentNotFoundException {
        List<Message> messagesEmbedded = chatRoom.getMessages();
        for(int i = 0; i < messagesEmbedded.size(); i++) {
            if (messagesEmbedded.get(i).getId().equals(messageCompleteDto.getId())) {
                Message previousMessage = chatRoom.getMessages().get(i);
                messageService.editInternal(previousMessage, messageCompleteDto);
            }
        }
        messageService.edit(messageCompleteDto);
    }
}
