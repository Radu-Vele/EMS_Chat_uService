package com.chatus.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class ChatRoom {
    @Id
    private String id;

    private List<Message> messages = new ArrayList<>();
}
