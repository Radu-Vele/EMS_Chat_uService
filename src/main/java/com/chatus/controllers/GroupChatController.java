package com.chatus.controllers;

import com.chatus.dtos.GroupCompleteDto;
import com.chatus.dtos.GroupCreateDto;
import com.chatus.dtos.GroupMemberDto;
import com.chatus.dtos.GroupMessageDto;
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
    public ResponseEntity<?> createGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, GroupCreateDto groupCreateDto) {
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

    @PutMapping("/addMessage")
    public ResponseEntity<?> addMessageToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody GroupMessageDto groupMessageDto) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.groupChatService.addMessage(groupMessageDto, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, String id) throws DocumentNotFoundException, ActionNotAllowedException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        this.groupChatService.delete(id, requesterEmailAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
git s