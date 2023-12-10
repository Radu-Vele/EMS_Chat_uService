package com.chatus.beans;

import com.chatus.data.User;
import com.chatus.data.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class WebSocketCustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        String name = httpServletRequest.getParameter("name");
        String role = httpServletRequest.getParameter("role");
        System.out.println("Received request ".concat(name).concat(" ").concat(role));
        if (role.equalsIgnoreCase("ADMIN")) {
            return new User(name, UserRole.ADMIN);
        }
        return new User(name, UserRole.USER);
    }
}
