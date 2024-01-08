package com.chatus.dtos.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MessageSaveDto {
    String senderEmail;
    String body;
    Long timestamp;

}