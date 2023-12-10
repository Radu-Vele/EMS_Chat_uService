package com.chatus.services;

import com.chatus.data.User;
import com.chatus.data.UserRole;
import com.chatus.exceptions.NoAdminOnlineException;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public List<String> getAllActiveConnections() {
        System.out.println(this.simpUserRegistry.getUserCount());
        return this.simpUserRegistry.getUsers().stream()
                .map(simpUser -> {
                    return Objects.requireNonNull(simpUser.getPrincipal()).toString();
                })
                .toList();
    }

    /**
     * @return the username of an online admin, randomly selected
     */
    public String findAdminAvailable() throws NoAdminOnlineException {
        Random random = new Random();
        List<String> adminList = this.simpUserRegistry.getUsers().stream()
                .filter(simpUser -> {
                    User user = (User) simpUser.getPrincipal();
                    if (user == null) {
                        return false;
                    }
                    return user.getRole().equals(UserRole.ADMIN);
                })
                .map(SimpUser::getName)
                .toList();
        if (adminList.isEmpty()) {
            throw new NoAdminOnlineException();
        }
        return adminList.get(random.nextInt(adminList.size()));
    }
}
