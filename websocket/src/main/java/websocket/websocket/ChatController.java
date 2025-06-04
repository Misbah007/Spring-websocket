package websocket.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatController {

    @MessageMapping("/chat.send")  // Maps to /app/chat.send
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) throws Exception {
        message.setTimestamp(Instant.now().toString());
        return message;
    }
}
