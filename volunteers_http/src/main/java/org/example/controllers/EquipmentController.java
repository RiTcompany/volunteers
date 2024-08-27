package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.table.EquipmentDto;
import org.example.pojo.filters.EquipmentFilter;
import org.example.services.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @PostMapping("/all_equipment")
    public ResponseEntity<List<EquipmentDto>> getEquipmentList(@RequestBody EquipmentFilter filter) {
        return ResponseEntity.ok(equipmentService.getEquipmentList(filter));
    }

    @PostMapping("/equipment")
    public ResponseEntity<Long> addEquipment(@RequestBody EquipmentDto equipmentDto) {
        return ResponseEntity.ok(equipmentService.addEquipment(equipmentDto));
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<Long> deleteEquipment(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.deleteEquipment(id));
    }
}
