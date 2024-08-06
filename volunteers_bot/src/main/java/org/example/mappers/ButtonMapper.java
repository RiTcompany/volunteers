package org.example.mappers;

import org.example.dto.ButtonDto;
import org.example.enums.ECity;
import org.example.enums.EEducationStatus;
import org.example.enums.EGender;
import org.springframework.stereotype.Component;

@Component
public class ButtonMapper {
    public ButtonDto buttonDto(ECity eCity, int row) {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setCallback(eCity.toString());
        buttonDto.setText(eCity.getString());
        buttonDto.setRow(row);
        return buttonDto;
    }

    public ButtonDto buttonDto(EGender eGender, int row) {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setCallback(eGender.toString());
        buttonDto.setText(eGender.getString());
        buttonDto.setRow(row);
        return buttonDto;
    }

    public ButtonDto buttonDto(EEducationStatus eEducationStatus, int row) {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setCallback(eEducationStatus.toString());
        buttonDto.setText(eEducationStatus.getString());
        buttonDto.setRow(row);
        return buttonDto;
    }
}
