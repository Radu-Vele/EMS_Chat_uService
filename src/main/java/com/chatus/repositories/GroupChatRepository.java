package com.chatus.repositories;

import com.chatus.entities.GroupChat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupChatRepository extends MongoRepository<GroupChat, String> {
}
