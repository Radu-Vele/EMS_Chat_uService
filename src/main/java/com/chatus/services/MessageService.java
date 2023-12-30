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
        return this.messageRepository.save(message).getId();
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
}
