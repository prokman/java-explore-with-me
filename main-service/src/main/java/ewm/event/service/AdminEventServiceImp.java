package ewm.event.service;

import ewm.categories.dto.CategoryMapper;
import ewm.categories.model.Category;
import ewm.categories.service.CatService;
import ewm.event.dto.*;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.model.StateAction;
import ewm.event.repository.EventRepository;
import ewm.exceptions.BadRequestException;
import ewm.exceptions.ConditionNotMeetException;
import ewm.exceptions.NotFoundException;
import ewm.location.model.Location;
import ewm.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImp implements AdminEventService {
    private final EventRepository eventRepository;
    private final CatService catService;
    private final LocationService locationService;

    @Override
    public List<EventFullDto> getAdminEvents(GetAdminEventsParam param, Integer from, Integer size) {
        if (size.equals(0)) {
            throw new BadRequestException("параметр size не может быть равен 0");
        }
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Event> page = eventRepository.findAll(EventAdminSpecification.withFilter(param), pageable);
        List<Event> foundEvents = page.getContent();
        return foundEvents.stream().map(EventMapper::eventToFullDto).toList();
    }

    @Override
    @Transactional
    public EventFullDto patchAdminEvents(UpdateEventAdminRequest request, Long eventId) {
        Event event = eventRepository.findWithDetailsById(eventId)
                .orElseThrow(() -> new NotFoundException("event с ИД" + eventId + " не найдено"));

        if (event.getEventState().equals(EventState.PUBLISHED)) {
            long minutes = ChronoUnit.MINUTES.between(event.getPublishedOn(), event.getEventDate());
            if (minutes < 60) {
                throw new ConditionNotMeetException("от даты публикации до даты события осталось менее часа");
            }
        }

        if (event.getEventState().equals(EventState.PUBLISHED)
                && request.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new ConditionNotMeetException("публикация уже опубликованного события");
        }
        if (event.getEventState().equals(EventState.PUBLISHED)
                && request.getStateAction().equals(StateAction.REJECT_EVENT)) {
            throw new ConditionNotMeetException("отмена уже опубликованного события");
        }
        if (event.getEventState().equals(EventState.CANCELED)
                && request.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new ConditionNotMeetException("публикация уже отмененного события");
        }

        Category category = null;
        Location location = null;
        if (request.getCategory() != null) {
            category = CategoryMapper
                    .dtoToCategory(catService.getCategoryById(request.getCategory()));
        }
        if (request.getLocation() != null) {
            location = locationService.postLocation(request.getLocation());
        }

        Event patchedEvent = updateEventFields(event, request, category, location);
        EventFullDto fullDto = EventMapper.eventToFullDto(eventRepository.save(patchedEvent));

        return fullDto;
    }

    private Event updateEventFields(Event target, UpdateEventAdminRequest source, Category category, Location location) {
        if (source.getAnnotation() != null) {
            target.setAnnotation(source.getAnnotation());
        }
        if (category != null) {
            target.setCategory(category);
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
        if (source.getEventDate() != null) {
            target.setEventDate(source.getEventDate());
        }
        if (location != null) {
            target.setLocation(location);
        }
        if (source.getPaid() != null) {
            target.setPaid(source.getPaid());
        }
        if (source.getParticipantLimit() != null) {
            target.setParticipantLimit(source.getParticipantLimit());
        }
        if (source.getRequestModeration() != null) {
            target.setRequestModeration(source.getRequestModeration());
        }
        if (source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }

        if (source.getStateAction() != null) {
            if (target.getEventState().equals(EventState.PENDING)
                    && source.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                target.setEventState(EventState.PUBLISHED);
                target.setPublishedOn(LocalDateTime.now());
            }


            if (!target.getEventState().equals(EventState.PUBLISHED)
                    && source.getStateAction().equals(StateAction.REJECT_EVENT)) {
                target.setEventState(EventState.CANCELED);
            }
        }


        return target;
    }
}
