package com.chatus.dtos.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MessageCompleteDto {
    String id;
    String senderEmail;
    String body;
    Long timestamp;
    Long seenTimestamp;
    Long deliveredTimestamp;
    String chatRoomId;
}

