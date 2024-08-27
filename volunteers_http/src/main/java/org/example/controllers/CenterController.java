package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.CenterDto;
import org.example.services.CenterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
