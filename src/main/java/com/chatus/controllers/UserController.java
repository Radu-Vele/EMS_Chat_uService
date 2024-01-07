package com.chatus.controllers;

import com.chatus.dtos.UserChatDto;
import com.chatus.dtos.UserCompleteDto;
import com.chatus.dtos.UserCreateDto;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.services.UserService;
import com.sun.net.httpserver.HttpsServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(this.userService.create(userCreateDto), HttpStatus.CREATED);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getUserById(@RequestParam String id) throws DocumentNotFoundException {
        return ResponseEntity.ok(this.userService.getById(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String id) {
        this.userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody UserCompleteDto userCompleteDto) throws DocumentNotFoundException {
        return ResponseEntity.ok(this.userService.edit(userCompleteDto));
    }

    @PutMapping("/addChat")
    public ResponseEntity<?> addChatToUser(@RequestBody UserChatDto userChatDto) throws DocumentNotFoundException {
        this.userService.addChat(userChatDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/removeChat")
    public ResponseEntity<?> removeChatFromUser(@RequestBody UserChatDto userChatDto) throws DocumentNotFoundException {
        this.userService.removeChat(userChatDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
