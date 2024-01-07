package com.chatus.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatThumbnailDto {
    String id;
    String endpoint1Email;
    String endpoint2Email;
    String mostRecentMessageBody;
}
