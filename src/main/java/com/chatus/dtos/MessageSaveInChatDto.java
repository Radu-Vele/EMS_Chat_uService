package com.chatus.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MessageSaveInChatDto {
    String senderEmail;
    String body;
    Long timestamp;
    String chatRoomId;
}
