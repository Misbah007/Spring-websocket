package websocket.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;

@Controller
@Slf4j
public class PrivateChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserSessionRegistry registry;

    @MessageMapping("/register")  // Called on connection with username
    public void registerUsername(@Header("simpSessionId") String sessionId,
                                 @Payload String username) {
        log.info("sessionId:{}", sessionId);
        registry.register(username, sessionId);
    }


    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload ChatMessage message) {
        String recipientSessionId = registry.getSessionId(message.getRecipient());
        log.info("recipientSessionId:{}", recipientSessionId);
        if (recipientSessionId != null) {
            messagingTemplate.convertAndSendToUser(
                    recipientSessionId,
                    "/queue/messages",
                    message,
                    createHeaders(recipientSessionId)
            );
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        log.info("createHeaders, sessionId:{} ", sessionId);
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
