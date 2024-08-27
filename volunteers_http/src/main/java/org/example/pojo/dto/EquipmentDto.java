package org.example.pojo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentDto {
    private Long id;
    private String type;
    private int year;
    private String currentOwner;
}
