package ewm.compilation.service;

import ewm.compilation.dto.CompilationDto;
import ewm.compilation.dto.CompilationMapper;
import ewm.compilation.dto.NewCompilationDto;
import ewm.compilation.dto.PatchCompilationDto;
import ewm.compilation.model.Compilation;
import ewm.compilation.repository.CompilationRepository;
import ewm.event.dto.EventMapper;
import ewm.event.dto.EventShortDto;
import ewm.event.model.Event;
import ewm.event.repository.EventRepository;
import ewm.event.service.PublicEventService;
import ewm.exceptions.BadRequestException;
import ewm.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImp implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final PublicEventService publicEventService;

    @Override
    @Transactional
    public CompilationDto postCompilation(NewCompilationDto request) {
        Set<Event> events = new HashSet<>();
        if (request.getPinned() == null) {
            request.setPinned(Boolean.FALSE);
        }
        if (request.getEvents() != null && !request.getEvents().isEmpty()) {
            events = request.getEvents()
                    .stream()
                    .map(eventId -> eventRepository.getReferenceById(eventId))
                    .collect(Collectors.toSet());
        }

        Compilation compilation = compilationRepository
                .save(CompilationMapper.requestToCompilation(request, events));
        return CompilationMapper.compilationToDto(compilation,
                publicEventService.getShrotDtoEventsSet(request.getEvents()));
    }

    @Override
    @Transactional
    public CompilationDto patchCompilation(PatchCompilationDto request, Long compId) {
        Compilation compilation = compilationRepository.findFullById(compId)
                .orElseThrow(() -> new NotFoundException("подборки с ИД " + compId + " не найдено"));
        CompilationDto compilationDto;

        if (request.getEvents() != null && !request.getEvents().isEmpty()) {
            Set<Event> events = request.getEvents()
                    .stream()
                    .map(eventId -> eventRepository.getReferenceById(eventId))
                    .collect(Collectors.toSet());

            Set<EventShortDto> eventShortDtoSet = publicEventService.getShrotDtoEventsSet(request.getEvents());
            compilation.setEvents(events);
            if (request.getPinned() != null) {
                compilation.setPinned(request.getPinned());
            }
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                compilation.setTitle(request.getTitle());
            }
            Compilation updatedCompilation = compilationRepository.save(compilation);
            compilationDto = CompilationMapper.compilationToDto(updatedCompilation, eventShortDtoSet);
            return compilationDto;
        } else {
            Set<EventShortDto> eventShortDtoSet = compilation.getEvents()
                    .stream()
                    .map(EventMapper::eventToShortDto)
                    .collect(Collectors.toSet());
            if (request.getPinned() != null) {
                compilation.setPinned(request.getPinned());
            }
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                compilation.setTitle(request.getTitle());
            }
            compilationDto = CompilationMapper.compilationToDto(compilation, eventShortDtoSet);
            return compilationDto;
        }
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Category with ID " + compId + " not found");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {

        Compilation compilation = compilationRepository.findFullById(compId)
                .orElseThrow(() -> new NotFoundException("подборки с ИД " + compId + " не найдено"));

        Set<EventShortDto> eventShortDtoSet = compilation.getEvents()
                .stream()
                .map(EventMapper::eventToShortDto)
                .collect(Collectors.toSet());
        CompilationDto compilationDto = CompilationMapper.compilationToDto(compilation, eventShortDtoSet);
        return compilationDto;
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        if (size.equals(0)) {
            throw new BadRequestException("параметр size не может быть равен 0");
        }
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("id").ascending());
        Page<Compilation> page = compilationRepository.getFullCompilations(pageable, pinned);
        List<Compilation> compilations = page.getContent();
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation comp : compilations) {
            Set<EventShortDto> eventShortDtoSet = comp.getEvents()
                    .stream()
                    .map(EventMapper::eventToShortDto)
                    .collect(Collectors.toSet());
            compilationDtoList.add(CompilationMapper.compilationToDto(comp, eventShortDtoSet));
        }
        return compilationDtoList;
    }
}
