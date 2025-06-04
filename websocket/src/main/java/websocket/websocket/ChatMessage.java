package websocket.websocket;

import lombok.Data;

@Data
public class ChatMessage {

    private String sender;
    private String content;
    private String recipient; // username or session id
    private String timestamp;

}
