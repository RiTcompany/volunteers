package org.example.pojo.dto.table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentDto {
    private Long id;
    private Long equipmentId;
    private String type;
    private int year;
    private String currentOwner;
}
