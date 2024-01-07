package com.chatus.dtos.groupChat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GroupMessageDto {
    String groupId;
    String messageId;
}
