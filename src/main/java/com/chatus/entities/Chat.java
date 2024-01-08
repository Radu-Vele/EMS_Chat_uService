package com.chatus.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("chats")
public class Chat extends ChatRoom{
    @DocumentReference(lazy = true)
    private User endpoint1;
    @DocumentReference(lazy = true)
    private User endpoint2;
}
