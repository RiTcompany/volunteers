package org.example.mappers;

import org.example.enums.ECity;
import org.example.pojo.dto.ButtonDto;
import org.springframework.stereotype.Component;

@Component
public class ButtonMapper {
    public ButtonDto buttonDto(ECity eCity, int row) {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setCallback(eCity.toString());
        buttonDto.setText(eCity.getCityName());
        buttonDto.setRow(row);
        return buttonDto;
    }
}
