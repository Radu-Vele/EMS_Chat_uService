package com.chatus.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("chats")
public class Chat {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    private User endpoint1;
    @DocumentReference(lazy = true)
    private User endpoint2;
    @DBRef
    private List<Message> messages;
}
