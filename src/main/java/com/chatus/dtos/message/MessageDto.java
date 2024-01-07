package com.chatus.dtos.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MessageDto {
    String senderEmailAddress;
    String receiverEmailAddress;
    String messageBody;
    Long timestamp;
}
