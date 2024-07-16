package org.example.mappers;

import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.KeyboardDto;
import org.example.pojo.entities.ChatHash;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyboardMapper {
    private static final String DEFAULT_MESSAGE_TEXT = "";

    public KeyboardDto keyboardDto(ChatHash chatHash, List<ButtonDto> buttonDtoList, String messageText) {
        KeyboardDto keyboardDto = keyboardDto(chatHash, buttonDtoList);
        setChatHashInfo(keyboardDto, chatHash);
        keyboardDto.setButtonDtoList(buttonDtoList);
        keyboardDto.setMessageText(messageText);
        return keyboardDto;
    }

    public KeyboardDto keyboardDto(ChatHash chatHash, List<ButtonDto> buttonDtoList) {
        KeyboardDto keyboardDto = new KeyboardDto();
        setChatHashInfo(keyboardDto, chatHash);
        keyboardDto.setButtonDtoList(buttonDtoList);
        keyboardDto.setMessageText(DEFAULT_MESSAGE_TEXT);
        return keyboardDto;
    }

    private void setChatHashInfo(KeyboardDto keyboardDto, ChatHash chatHash) {
        keyboardDto.setChatId(chatHash.getId());
        keyboardDto.setPageNumber(chatHash.getPrevBotMessagePageNumber());
        keyboardDto.setMessageId(chatHash.getPrevBotMessageId());
    }
}
