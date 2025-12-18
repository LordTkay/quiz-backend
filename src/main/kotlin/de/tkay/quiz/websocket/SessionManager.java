package de.tkay.quiz.websocket;

import org.springframework.web.socket.TextMessage;

public interface SessionManager {
    void broadcast(TextMessage message);
}
