package com.chatus.dtos;

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
    String senderEmailAddress;
    String body;
    Long timestamp;
    Long seenTimestamp;
    Long deliveredTimestamp;
}

