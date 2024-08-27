package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Equipment;
import org.example.mapper.EquipmentMapper;
import org.example.pojo.dto.table.EquipmentDto;
import org.example.pojo.filters.EquipmentFilter;
import org.example.repositories.EquipmentRepository;
import org.example.services.EquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;


    @Override
    public List<EquipmentDto> getEquipmentList(EquipmentFilter filter) {
        Stream<Equipment> stream = equipmentRepository.findAll().stream();
        stream = filterByType(stream, filter.getTypeList());
        return stream.map(equipmentMapper::equipmentDto).toList();
    }

    @Override
    public long addEquipment(EquipmentDto equipmentDto) {
        return equipmentRepository.saveAndFlush(equipmentMapper.equipment(equipmentDto)).getId();
    }

    @Override
    public long deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
        return id;
    }

    private Stream<Equipment> filterByType(Stream<Equipment> stream, List<String> typeList) {
        if (typeList != null) {
            return stream.filter(equipment -> typeList.contains(equipment.getType()));
        }

        return stream;
    }
}
