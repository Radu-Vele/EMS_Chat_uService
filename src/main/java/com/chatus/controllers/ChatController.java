package com.chatus.controllers;

import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.dtos.message.MessageSaveInChatDto;
import com.chatus.exceptions.ActionNotAllowedException;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.services.ChatService;
import com.chatus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatDb")
@RequiredArgsConstructor
public class ChatController {
    private final JwtUtil jwtUtil;
    private final ChatService chatService;

    @GetMapping("/getById")
    private ResponseEntity<?> getChatById(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam String chatId) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        return ResponseEntity.ok(this.chatService.getById(chatId, requesterEmailAddress));
    }

    @DeleteMapping("/deleteById")
    private ResponseEntity<?> deleteChatById(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam String chatId) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.chatService.deleteById(chatId, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addMessage")
    private ResponseEntity<?> addNewMessageToChat(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MessageSaveInChatDto messageSaveInChatDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        return ResponseEntity.ok(this.chatService.addNewMessage(messageSaveInChatDto, requesterEmailAddress));
    }

    @PutMapping("/editMessage")
    private ResponseEntity<?> editMessageFromChat(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MessageCompleteDto messageCompleteDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.chatService.editMessage(messageCompleteDto, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getMessagesBefore")
    private ResponseEntity<?> getMessagesFromChatBefore(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam String chatRoomId,
                                                        @RequestParam Long timestamp) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        return ResponseEntity.ok(this.chatService.getMessagesBefore(chatRoomId, requesterEmailAddress, timestamp));
    }
}
