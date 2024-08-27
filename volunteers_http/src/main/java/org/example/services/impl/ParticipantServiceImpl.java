package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.DateUtil;
import org.example.entities.Event;
import org.example.entities.Volunteer;
import org.example.mapper.ParticipialMapper;
import org.example.pojo.dto.DistrictParticipantDto;
import org.example.pojo.dto.EventParticipantDto;
import org.example.pojo.dto.VolunteerDto;
import org.example.pojo.filters.DistrictParticipantFilter;
import org.example.pojo.filters.EventParticipantFilter;
import org.example.pojo.filters.VolunteerFilter;
import org.example.repositories.VolunteerRepository;
import org.example.services.ParticipantService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final VolunteerRepository volunteerRepository;
    private final ParticipialMapper participialMapper;

    @Override
    public List<VolunteerDto> getVolunteerList(VolunteerFilter filter) {
        Stream<Volunteer> stream = volunteerRepository.findAll().stream();

        if (filter.getMinAge() != null) {
            stream = stream.filter(volunteer ->
                    filter.getMinAge() <= DateUtil.age(volunteer.getBirthday())
            );
        }

        if (filter.getMaxAge() != null) {
            stream = stream.filter(volunteer ->
                    filter.getMaxAge() >= DateUtil.age(volunteer.getBirthday())
            );
        }

        if (filter.getMinRank() != null) {
            stream = stream.filter(volunteer ->
                    volunteer.getRank() != null && filter.getMinRank() <= volunteer.getRank()
            );
        }

        if (filter.getColorList() != null) {
            stream = stream.filter(volunteer ->
                    filter.getColorList().contains(volunteer.getColor())
            );
        }

        if (filter.getEventIdList() != null) {
            stream = stream.filter(volunteer ->
                    volunteer.getEventList().stream()
                            .map(Event::getId)
                            .collect(Collectors.toSet())
                            .containsAll(filter.getEventIdList())
            );
        }

        if (filter.getLevelList() != null) {
            stream = stream.filter(volunteer ->
                    filter.getLevelList().contains(volunteer.getLevel())
            );
        }

        if (filter.getCenterIdList() != null) {
            stream = stream.filter(volunteer ->
                    filter.getCenterIdList().contains(volunteer.getCenterId())
            );
        }

        if (filter.isOrderByRankAsc()) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getRank));
        }

        if (filter.isOrderByRankDesc()) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getRank).reversed());
        }

        if (filter.isOrderByDateAsc()) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getBirthday));
        }

        if (filter.isOrderByDateDesc()) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getBirthday).reversed());
        }

        return stream.map(participialMapper::volunteerDto).toList();
    }

    @Override
    public List<DistrictParticipantDto> getDistrictParticipantList(DistrictParticipantFilter filter) {
        Stream<Volunteer> stream = volunteerRepository.findAll().stream();

        if (filter.getMinAge() != null) {
            stream = stream.filter(volunteer ->
                    filter.getMinAge() <= DateUtil.age(volunteer.getBirthday())
            );
        }

        if (filter.getMaxAge() != null) {
            stream = stream.filter(volunteer ->
                    filter.getMaxAge() >= DateUtil.age(volunteer.getBirthday())
            );
        }

        if (filter.getColorList() != null) {
            stream = stream.filter(volunteer ->
                    filter.getColorList().contains(volunteer.getColor())
            );
        }

        if (filter.getEventIdList() != null) {
            stream = stream.filter(volunteer ->
                    volunteer.getEventList().stream()
                            .map(Event::getId)
                            .collect(Collectors.toSet())
                            .containsAll(filter.getEventIdList())
            );
        }

        if (filter.isOrderByDateAsc()) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getBirthday));
        }

        if (filter.isOrderByDateDesc()) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getBirthday).reversed());
        }

        return stream.map(participialMapper::districtParticipantDto).toList();
    }

    @Override
    public List<EventParticipantDto> getEventParticipantList(EventParticipantFilter filter) {
        return null;
    }
}
