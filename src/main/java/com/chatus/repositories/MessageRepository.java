package com.chatus.repositories;

import com.chatus.entities.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    @Query(value = "{ 'chatRoom' : { $eq : ?0} , 'timestamp' : { $lt : ?1}}")
    List<Message> findMessagesFromChatroomSentBefore(String chatRoomId, Long timestamp, Pageable pageable);
}
