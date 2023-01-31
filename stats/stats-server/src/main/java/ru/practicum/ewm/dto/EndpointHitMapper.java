package ru.practicum.ewm.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    @Mapping(source = "timestamp", target = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);
}
