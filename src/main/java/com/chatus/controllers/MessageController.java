package com.chatus.controllers;

import com.chatus.dtos.message.MessageCompleteDto;
import com.chatus.dtos.message.MessageSaveDto;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.services.MessageService;
import com.chatus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/message")
@RestController
public class MessageController {
    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    @PostMapping("/save")
    public ResponseEntity<?> saveMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MessageSaveDto messageDto) {
        String emailAddress = jwtUtil.getUsernameFromToken(auth.substring(7));
        messageDto.setSenderEmail(emailAddress);
        return new ResponseEntity<String>(messageService.save(messageDto), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getMessage(@RequestParam String id) throws DocumentNotFoundException {
        return ResponseEntity.ok(messageService.getById(id));
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestBody MessageCompleteDto messageEditDto) throws DocumentNotFoundException {
        return ResponseEntity.ok(messageService.edit(messageEditDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMessage(@RequestParam String id) {
        messageService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

