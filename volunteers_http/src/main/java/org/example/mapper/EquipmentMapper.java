package org.example.mapper;

import org.example.entities.Equipment;
import org.example.pojo.dto.table.EquipmentDto;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {
    public EquipmentDto equipmentDto(Equipment equipment) {
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setId(equipment.getId());
        equipmentDto.setEquipmentId(equipment.getEquipmentId());
        equipmentDto.setType(equipment.getType());
        equipmentDto.setYear(equipment.getYear());
        equipmentDto.setCurrentOwner(equipment.getCurrentOwner());
        return equipmentDto;
    }

    public Equipment equipment(EquipmentDto equipmentDto) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentDto.getEquipmentId());
        equipment.setType(equipmentDto.getType());
        equipment.setYear(equipmentDto.getYear());
        equipment.setCurrentOwner(equipmentDto.getCurrentOwner());
        return equipment;
    }
}
