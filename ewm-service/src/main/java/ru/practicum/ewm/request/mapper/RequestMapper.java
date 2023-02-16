package ru.practicum.ewm.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

@Mapper (componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "event", source = "event.id")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);

    List<ParticipationRequestDto> toParticipationRequestDto(List<ParticipationRequest> participationRequest);
}
