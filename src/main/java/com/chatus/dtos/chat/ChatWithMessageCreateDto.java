package com.chatus.dtos.chat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatWithMessageCreateDto {
    String receiverEmail;
    String body;
    Long timestamp;
}