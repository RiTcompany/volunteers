package org.example.mappers;

import org.example.dto.ButtonDto;
import org.example.dto.KeyboardDto;
import org.example.entities.ChatHash;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyboardMapper {
    public KeyboardDto keyboardDto(ChatHash chatHash, List<ButtonDto> buttonDtoList) {
        KeyboardDto keyboardDto = new KeyboardDto();
        keyboardDto.setChatId(chatHash.getId());
        keyboardDto.setPageNumber(chatHash.getPrevBotMessagePageNumber());
        keyboardDto.setMessageId(chatHash.getPrevBotMessageId());
        keyboardDto.setButtonDtoList(buttonDtoList);
        return keyboardDto;
    }
}
