package org.example.mapper;

import org.example.entities.Equipment;
import org.example.pojo.dto.EquipmentDto;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {
    public EquipmentDto equipmentDto(Equipment equipment) {
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setId(equipment.getId());
        equipmentDto.setType(equipment.getType());
        equipmentDto.setYear(equipment.getYear());
        equipmentDto.setCurrentOwner(equipment.getCurrentOwner());
        return equipmentDto;
    }
}
