package com.chatus.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
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
    // TODO: add boolean was-deleted (mark as deleted but still show something)
}
