package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.table.CenterDto;
import org.example.services.CenterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CenterController {
    private final CenterService centerService;

    @GetMapping("/center")
    public ResponseEntity<List<CenterDto>> getCenterList() {
        return ResponseEntity.ok(centerService.getCenterList());
    }

    @PostMapping("/center")
    public ResponseEntity<Long> addCenter(@RequestBody CenterDto centerDto) {
        return ResponseEntity.ok(centerService.addCenter(centerDto));
    }

    @DeleteMapping("/center/{id}")
    public ResponseEntity<Long> deleteCenter(@PathVariable Long id) {
        return ResponseEntity.ok(centerService.deleteCenter(id));
    }
}
