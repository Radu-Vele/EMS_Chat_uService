package com.chatus.dtos.groupChat;

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
public class GroupCompleteDto {
    String id;
    String name;
    List<String> membersEmails;
    List<MessageCompleteDto> recentMessages;
}

