package com.chatus.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MessageDto {
    String senderEmailAddress;
    String receiverEmailAddress;
    String messageBody;
    Long timestamp;
}
