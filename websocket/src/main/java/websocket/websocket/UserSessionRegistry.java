package websocket.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class UserSessionRegistry implements ApplicationListener<SessionConnectEvent> {

    private final Map<String, String> usernameToSessionId = new ConcurrentHashMap<>();
    private final Map<String, String> sessionIdToUsername = new ConcurrentHashMap<>();

    public void register(String username, String sessionId) {
        usernameToSessionId.put(username, sessionId);
        sessionIdToUsername.put(sessionId, username);
    }

    public String getSessionId(String username) {
        return usernameToSessionId.get(username);
    }

    public String getUsername(String sessionId) {
        return sessionIdToUsername.get(sessionId);
    }

    public void removeBySessionId(String sessionId) {
        String username = sessionIdToUsername.remove(sessionId);
        if (username != null) {
            usernameToSessionId.remove(username);
        }
    }

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        log.info("Events :{}", event);
        // You can log session IDs or register if you extract headers
    }
}

