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
@Document("group_chats")
public class GroupChat {
    @Id
    private String id;
    private String name;
    @DocumentReference(lazy = true)
    private List<User> members;
    @DBRef
    private List<Message> messages;
}
