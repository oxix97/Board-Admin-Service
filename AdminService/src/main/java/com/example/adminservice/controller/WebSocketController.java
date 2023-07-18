package com.example.adminservice.controller;

import com.example.adminservice.dto.websocket.WebSocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/chat")
    public WebSocketMessage chat(WebSocketMessage message, Principal principal) throws Exception {
        Thread.sleep(1000L);
        return WebSocketMessage.of("Hi " + principal.getName() + "! " + message.content() + "?");
    }
}
