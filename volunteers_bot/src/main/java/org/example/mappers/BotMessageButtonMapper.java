package org.example.mappers;

import org.example.entities.BotMessageButton;
import org.springframework.stereotype.Component;

@Component
public class BotMessageButtonMapper {
    public BotMessageButton botMessageButton(long botMessageId, String buttonName, String buttonLink) {
        BotMessageButton botMessageButton = new BotMessageButton();
        botMessageButton.setBotMessageId(botMessageId);
        botMessageButton.setButtonName(buttonName);
        botMessageButton.setButtonLink(buttonLink);
        return botMessageButton;
    }
}
