package org.example.entities;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("chat")
@Getter
@Setter
public class ChatHash implements Serializable {
    @Id
    private Long id;
    private EConversation eConversation;
    private EConversationStep eConversationStep;
    private Integer prevBotMessageId;
    private Integer prevBotMessagePageNumber;

    public void setDefaultPrevBotMessagePageNumber() {
        prevBotMessagePageNumber = 0;
    }

}
