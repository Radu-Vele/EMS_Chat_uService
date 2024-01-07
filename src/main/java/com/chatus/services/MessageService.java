package com.chatus.services;

import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.dtos.message.MessageSaveDto;
import com.chatus.entities.Message;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chatus.constants.DatabaseConstants.MESSAGE_BULK_SIZE;

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
                .senderEmail(message.getSenderEmail())
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
        this.editInternal(messagePrv, messageEditDto);
        return this.messageRepository.save(messagePrv).getId();
    }

    public void editInternal(Message previous, MessageCompleteDto messageCompleteDto) {
        if (previous.getSeenTimestamp() == null) {
            previous.setSeenTimestamp(messageCompleteDto.getSeenTimestamp());
        }
        if (previous.getDeliveredTimestamp() == null) {
            previous.setDeliveredTimestamp(messageCompleteDto.getDeliveredTimestamp());
        }
    }

    public void delete(String id) {
        this.messageRepository.deleteById(id);
    }

    public Message saveInternal(Message newMessage) {
        return this.messageRepository.insert(newMessage);
    }

    public List<MessageCompleteDto> getMessagesBefore(String chatRoomId, Long timestamp) {
        Pageable pageable = PageRequest.of(0,
                MESSAGE_BULK_SIZE,
                Sort.by(Sort.Order.desc("timestamp")));
        List<Message> messages = this.messageRepository.findMessagesFromChatroomSentBefore(chatRoomId, timestamp, pageable);
        return messages.stream()
                .map(m -> this.modelMapper.map(m, MessageCompleteDto.class))
                .toList();
    }
}
