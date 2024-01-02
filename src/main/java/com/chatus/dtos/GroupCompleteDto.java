package com.chatus.dtos;

import com.chatus.entities.Message;
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
    List<Message> recentMessages;
}
