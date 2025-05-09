package ewm.event.service;

import ewm.event.dto.*;
import ewm.event.model.Event;
import ewm.event.repository.EventRepository;
import ewm.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stat.StatsClient;
import statdto.StatDtoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImp implements PublicEventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient = new StatsClient();

    @Override
    public EventFullDto getPublicEventById(Long eventId) {
        Event event = eventRepository.findWithDetailsById(eventId)
                .orElseThrow(() -> new NotFoundException("event " + eventId + " не найдено"));
        if (event.getPublishedOn() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String rangeStart = formatter.format(LocalDateTime.of(1500, 01, 01, 00, 00, 00));
            String rangeEnd = formatter.format(LocalDateTime.now());
            List<String> uris = new ArrayList<>();
            uris.add("/events/" + eventId);


            List<StatDtoResponse> statDtoResponse = statsClient
                    .getStats(rangeStart, rangeEnd, Optional.of(uris), Optional.of(Boolean.TRUE));

            Long views;
            if (statDtoResponse == null || statDtoResponse.isEmpty()) {
                views = 0L;
            } else {
                views = statDtoResponse.getFirst().getHits();
            }

            event.setViews(views);
            return EventMapper.eventToFullDto(event);
        } else {
            throw new NotFoundException("нет опубликованного события с ид №" + eventId);
        }

    }

    @Override
    public List<EventFullDto> getAllPublicEventByParam(GetPublicEventsParam param, Integer from, Integer size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Event> page = eventRepository.findAll(EventPublicSpecification.withFilter(param), pageable);
        List<Event> foundEvents = page.getContent();
        return foundEvents.stream().map(EventMapper::eventToFullDto).toList();
    }

    @Override
    public Set<EventShortDto> getShrotDtoEventsSet(Set<Long> eventIds) {
        return eventRepository.findByIdIn(eventIds)
                .stream()
                .map(EventMapper::eventToShortDto)
                .collect(Collectors.toSet());
    }

}
