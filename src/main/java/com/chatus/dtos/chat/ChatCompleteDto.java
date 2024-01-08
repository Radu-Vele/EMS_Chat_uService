package com.chatus.dtos.chat;

import com.chatus.dtos.message.MessageCompleteDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatCompleteDto {
    String id;
    String endpoint1Email;
    String endpoint2Email;
    List<MessageCompleteDto> messageCompleteDtoList;
}
