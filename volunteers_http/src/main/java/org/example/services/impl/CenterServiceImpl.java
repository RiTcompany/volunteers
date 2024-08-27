package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.CenterMapper;
import org.example.pojo.dto.table.CenterDto;
import org.example.repositories.CenterRepository;
import org.example.services.CenterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {
    private final CenterRepository centerRepository;
    private final CenterMapper centerMapper;

    @Override
    public List<CenterDto> getCenterList() {
        return centerRepository.findAll().stream().map(centerMapper::centerDto).toList();
    }

    @Override
    public long addCenter(CenterDto centerDto) {
        return centerRepository.saveAndFlush(centerMapper.center(centerDto)).getId();
    }

    @Override
    public long deleteCenter(Long id) {
        centerRepository.deleteById(id);
        return id;
    }
}
