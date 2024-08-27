package org.example.services;

import org.example.pojo.dto.EquipmentDto;
import org.example.pojo.filters.EquipmentFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EquipmentService {
    List<EquipmentDto> getEquipmentList(EquipmentFilter filter);
}
