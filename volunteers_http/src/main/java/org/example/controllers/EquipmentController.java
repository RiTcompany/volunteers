package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.CenterDto;
import org.example.pojo.dto.EquipmentDto;
import org.example.pojo.filters.EquipmentFilter;
import org.example.services.EquipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @GetMapping("/equipment")
    public ResponseEntity<List<EquipmentDto>> getCenterList(@RequestBody EquipmentFilter filter) {
        return ResponseEntity.ok(equipmentService.getEquipmentList(filter));
    }
}
