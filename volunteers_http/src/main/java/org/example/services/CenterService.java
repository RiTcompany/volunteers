package org.example.services;

import org.example.pojo.dto.CenterDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CenterService {
    List<CenterDto> getCenterList();
}
