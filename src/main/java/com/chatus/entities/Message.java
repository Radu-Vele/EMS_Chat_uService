package com.chatus.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("messages")
public class Message {
    @Id
    private String id;
    private String senderEmail;
    private String body;
    private Long timestamp;
    private Long deliveredTimestamp;
    private Long seenTimestamp;
    private String chatRoom;
    // TODO: add boolean was-deleted (mark as deleted but still show something)
}
