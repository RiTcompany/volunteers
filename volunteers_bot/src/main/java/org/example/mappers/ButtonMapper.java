package org.example.mappers;

import org.example.dto.ButtonDto;
import org.example.enums.ECity;
import org.example.enums.EEducationStatus;
import org.example.enums.EGender;
import org.springframework.stereotype.Component;

@Component
public class ButtonMapper {
    public ButtonDto buttonDto(ECity eCity) {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setCallback(eCity.toString());
        buttonDto.setText(eCity.getValue());
        return buttonDto;
    }

    public ButtonDto buttonDto(EGender eGender) {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setCallback(eGender.toString());
        buttonDto.setText(eGender.getValue());
        return buttonDto;
    }

    public ButtonDto buttonDto(EEducationStatus eEducationStatus) {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setCallback(eEducationStatus.toString());
        buttonDto.setText(eEducationStatus.getValue());
        return buttonDto;
    }
}
