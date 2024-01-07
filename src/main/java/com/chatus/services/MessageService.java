package com.chatus.services;

import com.chatus.dtos.MessageCompleteDto;
import com.chatus.dtos.MessageSaveDto;
import com.chatus.entities.Message;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    public String save(MessageSaveDto messageDto) {
        Message message = this.modelMapper.map(messageDto, Message.class);
        return this.saveInternal(message).getId();
    }

    public MessageCompleteDto getById(String id) throws DocumentNotFoundException {
        Message message = this.messageRepository.findById(id)
                .orElseThrow(DocumentNotFoundException::new);
        return MessageCompleteDto.builder()
                .id(message.getId())
                .senderEmailAddress(message.getSenderEmail())
                .body(message.getBody())
                .timestamp(message.getTimestamp())
                .seenTimestamp(message.getSeenTimestamp())
                .deliveredTimestamp(message.getDeliveredTimestamp())
                .build();
    }

    public Message getMessageObjectById (String id) throws DocumentNotFoundException {
        return this.messageRepository.findById(id)
                .orElseThrow(DocumentNotFoundException::new);
    }

    public String edit(MessageCompleteDto messageEditDto) throws DocumentNotFoundException {
        Message messagePrv = this.messageRepository
                .findById(messageEditDto.getId())
                .orElseThrow(DocumentNotFoundException::new);
        // TODO: treat body edit case (mark message as edited)
        // Modifiable values
        if (messagePrv.getSeenTimestamp() == null) {
            messagePrv.setSeenTimestamp(messageEditDto.getSeenTimestamp());
        }
        if (messagePrv.getDeliveredTimestamp() == null) {
            messagePrv.setDeliveredTimestamp(messageEditDto.getDeliveredTimestamp());
        }
        return this.messageRepository.save(messagePrv).getId();
    }

    public void delete(String id) {
        this.messageRepository.deleteById(id);
    }

    public Message saveInternal(Message newMessage) {
        return this.messageRepository.insert(newMessage);
    }
}
