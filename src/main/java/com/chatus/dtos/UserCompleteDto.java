package com.chatus.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserCompleteDto {
    String id;
    String emailAddress;
    String role;
    String status;
    List<ChatThumbnailDto> chatThumbnailDtoList;
}
