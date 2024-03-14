package com.chatus.controllers;

import com.chatus.dtos.groupChat.GroupCompleteDto;
import com.chatus.dtos.groupChat.GroupCreateDto;
import com.chatus.dtos.groupChat.GroupMemberDto;
import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.dtos.message.MessageSaveInChatDto;
import com.chatus.exceptions.ActionNotAllowedException;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.services.GroupChatService;
import com.chatus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupChatController {
    private final GroupChatService groupChatService;
    private final JwtUtil jwtUtil;
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody GroupCreateDto groupCreateDto) throws DocumentNotFoundException {
        String creatorEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
            if (!groupCreateDto.getMemberEmails().contains(creatorEmailAddress)) { //make sure the person requesting is in the group
            groupCreateDto.getMemberEmails().add(creatorEmailAddress);
        }
        return new ResponseEntity<>(this.groupChatService.create(groupCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/rename")
    public ResponseEntity<?> renameGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody GroupCompleteDto groupCompleteDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.groupChatService.rename(groupCompleteDto, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/addMember")
    public ResponseEntity<?> addMemberToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody GroupMemberDto groupMemberDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.groupChatService.addMember(groupMemberDto, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/removeMember")
    public ResponseEntity<?> removeMemberFromGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody GroupMemberDto groupMemberDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.groupChatService.removeMember(groupMemberDto, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping    ("/addMessage")
    public ResponseEntity<?> addMessageToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MessageSaveInChatDto groupMessageDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        return ResponseEntity.ok(this.groupChatService.createMessageAndAddToGroup(groupMessageDto, requesterEmailAddress));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, String id) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.groupChatService.delete(id, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getById(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam String id) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        return ResponseEntity.ok(this.groupChatService.getById(id, requesterEmailAddress));
    }

    @PutMapping("/editMessage")
    public ResponseEntity<?> getById(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MessageCompleteDto messageCompleteDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.groupChatService.editMessage(messageCompleteDto, requesterEmailAddress);
        return new ResponseEntity<> (HttpStatus.OK);
    }

    @GetMapping("/getMessagesBefore")
    public ResponseEntity<?> getMessagesFromGroupBefore(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam String chatRoomId, @RequestParam Long timestamp) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        return ResponseEntity.ok(this.groupChatService.getMessagesBefore(chatRoomId, timestamp, requesterEmailAddress));
    }
}