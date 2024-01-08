package com.chatus.controllers;

import com.chatus.dtos.chat.ChatWithMessageCreateDto;
import com.chatus.dtos.user.UserChatDto;
import com.chatus.dtos.user.UserCompleteDto;
import com.chatus.dtos.user.UserCreateDto;
import com.chatus.exceptions.DocumentNotFoundException;
import com.chatus.services.UserService;
import com.chatus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

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

    @PostMapping("/addNewChat")
    public ResponseEntity<?> addNewChatToUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,
                                              @RequestBody ChatWithMessageCreateDto newChatDto) throws DocumentNotFoundException {
        String requesterEmailAddress = this.jwtUtil.getUsernameFromBearerTokenAuthHeader(auth);
        return new ResponseEntity<>(this.userService.addNewChat(newChatDto, requesterEmailAddress), HttpStatus.CREATED);
    }

    @PutMapping("/removeChat")
    public ResponseEntity<?> removeChatFromUser(@RequestBody UserChatDto userChatDto) throws DocumentNotFoundException {
        this.userService.removeChat(userChatDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
