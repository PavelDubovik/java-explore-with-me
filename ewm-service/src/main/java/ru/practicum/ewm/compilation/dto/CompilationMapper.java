package ru.practicum.ewm.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.mapper.EventMapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target="events", source="events")
    Compilation toCompilation(NewCompilationDto newCompilationDto, Set<EventDto> events);

    @Mapping(target="events", source="events")
    @Mapping(target = "id", source = "compId")
    Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest, Set<EventDto> events, Long compId);

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> toCompilationDto(List<Compilation> compilations);
}
