package com.chatus.controllers;

import com.chatus.dtos.MessageSaveDto;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/message")
@RestController
public class MessageController {
    private final MessageService messageService;
    @PostMapping("/save")
    public ResponseEntity<?> saveMessage(@RequestBody MessageSaveDto messageDto) {
        return new ResponseEntity<String>(messageService.save(messageDto), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getMessage(@RequestParam String id) throws DocumentNotFoundException {
        return ResponseEntity.ok(messageService.getById(id));
    }
}

