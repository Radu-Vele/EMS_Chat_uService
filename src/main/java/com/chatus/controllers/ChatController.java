package com.chatus.controllers;

import com.chatus.dtos.chat.ChatWithMessageCreateDto;
import com.chatus.services.ChatService;
import com.chatus.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatDb")
@RequiredArgsConstructor
public class ChatController {
    private final JwtUtil jwtUtil;
    private final ChatService chatService;
}
