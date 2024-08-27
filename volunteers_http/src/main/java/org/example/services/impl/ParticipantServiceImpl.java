package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.DateUtil;
import org.example.entities.Event;
import org.example.entities.Volunteer;
import org.example.enums.EColor;
import org.example.mapper.ParticipialMapper;
import org.example.pojo.dto.CenterParticipantDto;
import org.example.pojo.dto.DistrictParticipantDto;
import org.example.pojo.dto.EventParticipantDto;
import org.example.pojo.dto.VolunteerDto;
import org.example.pojo.filters.CenterParticipantFilter;
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

        stream = filterByMinAge(stream, filter.getMinAge());
        stream = filterByMaxAge(stream, filter.getMaxAge());
        stream = filterByMinRank(stream, filter.getMinRank());

        stream = filterByColor(stream, filter.getColorList());
        stream = filterByEvent(stream, filter.getEventIdList());
        stream = filterByLevel(stream, filter.getLevelList());
        stream = filterByCenter(stream, filter.getCenterIdList());

        stream = sortByRankDesc(
                sortByRankAsc(
                        stream, filter.isOrderByRankAsc()
                ), filter.isOrderByRankDesc()
        );

        stream = sortByDateDesc(
                sortByDateAsc(
                        stream, filter.isOrderByDateAsc()
                ), filter.isOrderByDateDesc()
        );

        return stream.map(participialMapper::volunteerDto).toList();
    }

    @Override
    public Long deleteVolunteer(Long id) {
        volunteerRepository.deleteById(id);
        return id;
    }

    @Override
    public List<DistrictParticipantDto> getDistrictParticipantList(long districtTeamId, DistrictParticipantFilter filter) {
        Stream<Volunteer> stream = volunteerRepository.findAllByDistrictTeamId(districtTeamId).stream();

        stream = filterByMinAge(stream, filter.getMinAge());
        stream = filterByMaxAge(stream, filter.getMaxAge());

        stream = filterByColor(stream, filter.getColorList());
        stream = filterByEvent(stream, filter.getEventIdList());

        stream = sortByDateDesc(
                sortByDateAsc(
                        stream, filter.isOrderByDateAsc()
                ), filter.isOrderByDateDesc()
        );

        return stream.map(participialMapper::districtParticipantDto).toList();
    }

    @Override
    public List<EventParticipantDto> getEventParticipantList(long eventId, EventParticipantFilter filter) {
        Stream<Volunteer> stream = volunteerRepository.findAll().stream(); // TODO : filter by event

        stream = filterByMinAge(stream, filter.getMinAge());
        stream = filterByMaxAge(stream, filter.getMaxAge());
        stream = filterByMinRank(stream, filter.getMinRank());
//        TODO : filter by functional
        stream = filterByTesting(stream, filter.getTesting());
        stream = filterByClothes(stream, filter.getHasClothes());

        stream = sortByRankDesc(
                sortByRankAsc(
                        stream, filter.isOrderByRankAsc()
                ), filter.isOrderByRankDesc()
        );

        stream = sortByDateDesc(
                sortByDateAsc(
                        stream, filter.isOrderByDateAsc()
                ), filter.isOrderByDateDesc()
        );

        return stream.map(participialMapper::eventParticipantDto).toList();
    }

    @Override
    public List<CenterParticipantDto> getCenterParticipantList(long centerId, CenterParticipantFilter filter) {
        Stream<Volunteer> stream = volunteerRepository.findAllByCenterId(centerId).stream();

        stream = filterByMinAge(stream, filter.getMinAge());
        stream = filterByMaxAge(stream, filter.getMaxAge());
        stream = filterByMinRank(stream, filter.getMinRank());
        stream = filterByInterview(stream, filter.getHasInterview());
        stream = filterByLevel(stream, filter.getLevelList());

        stream = filterByColor(stream, filter.getColorList());
        stream = filterByEvent(stream, filter.getEventIdList());

        stream = sortByRankDesc(
                sortByRankAsc(
                        stream, filter.isOrderByRankAsc()
                ), filter.isOrderByRankDesc()
        );

        stream = sortByDateDesc(
                sortByDateAsc(
                        stream, filter.isOrderByDateAsc()
                ), filter.isOrderByDateDesc()
        );

        return stream.map(participialMapper::centerParticipantDto).toList();
    }

    private Stream<Volunteer> filterByMinAge(Stream<Volunteer> stream, Integer minAge) {
        if (minAge != null) {
            return stream.filter(volunteer ->
                    minAge <= DateUtil.age(volunteer.getBirthday())
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByMaxAge(Stream<Volunteer> stream, Integer maxAge) {
        if (maxAge != null) {
            return stream.filter(volunteer ->
                    maxAge >= DateUtil.age(volunteer.getBirthday())
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByMinRank(Stream<Volunteer> stream, Double minRank) {
        if (minRank != null) {
            stream = stream.filter(volunteer ->
                    volunteer.getRank() != null && minRank <= volunteer.getRank()
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByColor(Stream<Volunteer> stream, List<EColor> colorList) {
        if (colorList != null) {
            stream = stream.filter(volunteer ->
                    colorList.contains(volunteer.getColor())
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByEvent(Stream<Volunteer> stream, List<Long> eventIdList) {
        if (eventIdList != null) {
            stream = stream.filter(volunteer ->
                    volunteer.getEventList().stream()
                            .map(Event::getId)
                            .collect(Collectors.toSet())
                            .containsAll(eventIdList)
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByLevel(Stream<Volunteer> stream, List<String> levelList) {
        if (levelList != null) {
            stream = stream.filter(volunteer ->
                    levelList.contains(volunteer.getLevel())
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByCenter(Stream<Volunteer> stream, List<Long> centerIdList) {
        if (centerIdList != null) {
            stream = stream.filter(volunteer ->
                    centerIdList.contains(volunteer.getCenter().getId())
            );
        }

        return stream;
    }

    private Stream<Volunteer> sortByRankAsc(Stream<Volunteer> stream, boolean orderByRankAsc) {
        if (orderByRankAsc) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getRank));
        }

        return stream;
    }

    private Stream<Volunteer> sortByRankDesc(Stream<Volunteer> stream, boolean orderByRankDesc) {
        if (orderByRankDesc) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getRank).reversed());
        }

        return stream;
    }

    private Stream<Volunteer> sortByDateAsc(Stream<Volunteer> stream, boolean orderByDateAsc) {
        if (orderByDateAsc) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getBirthday));
        }

        return stream;
    }

    private Stream<Volunteer> sortByDateDesc(Stream<Volunteer> stream, boolean orderByDateDesc) {
        if (orderByDateDesc) {
            stream = stream.sorted(Comparator.comparing(Volunteer::getBirthday).reversed());
        }

        return stream;
    }

    private Stream<Volunteer> filterByTesting(Stream<Volunteer> stream, Boolean testing) {
        if (testing != null) {
            stream = stream.filter(volunteer ->
                    testing.equals(volunteer.isTesting())
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByClothes(Stream<Volunteer> stream, Boolean hasClothes) {
        if (hasClothes != null) {
            stream = stream.filter(volunteer ->
                    hasClothes.equals(volunteer.getHasAnorak())
            );
        }

        return stream;
    }

    private Stream<Volunteer> filterByInterview(Stream<Volunteer> stream, Boolean interview) {
        if (interview != null) {
            stream = stream.filter(volunteer ->
                    interview.equals(volunteer.isHasInterview())
            );
        }

        return stream;
    }
}
