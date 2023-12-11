package com.chatus.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SeenNotificationDto {
    @JsonProperty("messageTimestamp")
    Long messageTimestamp;
}
