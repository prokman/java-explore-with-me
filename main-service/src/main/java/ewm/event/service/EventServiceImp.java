package ewm.event.service;

import ewm.categories.dto.CategoryMapper;
import ewm.categories.model.Category;
import ewm.categories.service.CatService;
import ewm.event.dto.*;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.model.StateAction;
import ewm.event.repository.EventRepository;
import ewm.exceptions.ConditionNotMeetException;
import ewm.exceptions.NotFoundException;
import ewm.location.model.Location;
import ewm.location.service.LocationService;
import ewm.requests.dto.RequestDto;
import ewm.requests.model.RequestStatus;
import ewm.requests.service.RequestService;
import ewm.user.dto.UserDto;
import ewm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImp implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CatService catService;
    private final LocationService locationService;
    private final RequestService requestService;

    @Override
    @Transactional
    public EventFullDto postEvent(NewEventDto request, Long userId) {
        Category category = CategoryMapper.dtoToCategory(catService.getCategoryById(request.getCategory()));
        Location location = locationService.postLocation(request.getLocation());
        UserDto userDto = userService.getUsers(null, null, List.of(userId)).getFirst();
        Event event1 = EventMapper.requestToEvent(request, category, userDto, location);
        Event event = eventRepository.save(event1);
        return EventMapper.eventToFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto patchEvent(UpdateEventUserRequest request, Long userId, Long eventId) {
        Event event = eventRepository.findWithDetailsById(eventId)
                .orElseThrow(() -> new NotFoundException("event с ИД" + eventId + " не найдено"));
        if (event.getEventState().equals(EventState.PUBLISHED)) {
            throw new ConditionNotMeetException("Only pending or canceled events can be changed");
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

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        List<Event> eventList = eventRepository.getFullEventsByParam(pageable);
        return eventList.stream().map(EventMapper::eventToShortDto).toList();
    }

    @Override
    public EventFullDto getEventsById(Long userId, Long eventId) {
        Event event = eventRepository
                .findWithDetailsById(eventId)
                .orElseThrow(() -> new NotFoundException("event с ИД" + eventId + " не найдено"));
        return EventMapper.eventToFullDto(event);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult patchRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findWithDetailsById(eventId)
                .orElseThrow(() -> new NotFoundException("event с ИД" + eventId + " не найдено"));
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConditionNotMeetException("заявки не требуют подтверждения");
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit().longValue()) {
            throw new ConditionNotMeetException("достигнут лимит подтвержденных заявок");
        }
        if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
            Integer updateSize = request.getRequestIds().size();
            Long maxForUpdate = event.getParticipantLimit() - event.getConfirmedRequests();
            if (updateSize <= maxForUpdate) {
                Long confirmedRequests = event.getConfirmedRequests();
                confirmedRequests = confirmedRequests + updateSize;
                event.setConfirmedRequests(confirmedRequests);
                eventRepository.save(event);
                return new EventRequestStatusUpdateResult(aproveRequest(request.getRequestIds()), Collections.emptyList());
            } else {
                List<Long> forAprove = request.getRequestIds().subList(0, maxForUpdate.intValue());
                List<Long> forReject = request.getRequestIds().subList(maxForUpdate.intValue(), updateSize);

                Long confirmedRequests = event.getConfirmedRequests();
                confirmedRequests = confirmedRequests + maxForUpdate;
                event.setConfirmedRequests(confirmedRequests);
                eventRepository.save(event);
                return new EventRequestStatusUpdateResult(aproveRequest(forAprove), rejectRequest(forReject));
            }
        } else {
            return new EventRequestStatusUpdateResult(Collections.emptyList(), rejectRequest(request.getRequestIds()));
        }
    }

    @Override
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        return requestService.getRequestByEventId(eventId);
    }

    private Event updateEventFields(Event target, UpdateEventUserRequest source, Category category, Location location) {
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
            if (source.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                target.setEventState(EventState.CANCELED);
            }
            if (source.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                target.setEventState(EventState.PENDING);
            }
        }
        return target;
    }

    private List<RequestDto> aproveRequest(List<Long> requestIds) {
        List<RequestDto> requestDtoAprovedList = requestService.getRequestByIds(requestIds);
        for (RequestDto requestDto : requestDtoAprovedList) {
            if (requestDto.getStatus().equals(RequestStatus.PENDING)) {
                requestDto.setStatus(RequestStatus.CONFIRMED);
            } else {
                throw new ConditionNotMeetException("заявка №" + requestDto.getId() + " уже обработана и имеет статус " +
                        requestDto.getStatus());
            }
        }
        List<RequestDto> confirmedRequests = requestService.patchRequestByIds(requestDtoAprovedList);
        return confirmedRequests;
    }

    private List<RequestDto> rejectRequest(List<Long> requestIds) {
        List<RequestDto> requestDtoRejectedList = requestService.getRequestByIds(requestIds);
        for (RequestDto requestDto : requestDtoRejectedList) {
            if (requestDto.getStatus().equals(RequestStatus.PENDING)) {
                requestDto.setStatus(RequestStatus.REJECTED);
            } else {
                throw new ConditionNotMeetException("заявка №" + requestDto.getId() + " уже обработана и имеет статус " +
                        requestDto.getStatus());
            }
        }
        List<RequestDto> rejectedRequests = requestService.patchRequestByIds(requestDtoRejectedList);
        return rejectedRequests;
    }


}
