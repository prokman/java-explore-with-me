package ewm.event.dto;

import ewm.categories.model.Category;
import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.location.dto.LocationMapper;
import ewm.location.model.Location;
import ewm.user.dto.UserDto;
import ewm.user.dto.UserMapper;

import java.time.LocalDateTime;

public class EventMapper {
    public static Event requestToEvent(NewEventDto request, Category category, UserDto initiator, Location location) {
        Long defaultConfirmedRequests = 0L;
        Long defaultViews = 0L;
        Event event = new Event();
        event.setAnnotation(request.getAnnotation());
        event.setCategory(category);
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        if (request.getPaid() == null) {
            event.setPaid(Boolean.FALSE);
        } else {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() == null) {
            event.setRequestModeration(Boolean.TRUE);
        } else {
            event.setRequestModeration(request.getRequestModeration());
        }
        event.setTitle(request.getTitle());
        event.setLocation(location);
        event.setConfirmedRequests(defaultConfirmedRequests);
        event.setEventState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(UserMapper.dtoToUser(initiator));
        event.setViews(defaultViews);

        return event;
    }

    public static EventShortDto eventToShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(event.getCategory());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UserMapper.userToShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }

    public static EventFullDto eventToFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();

        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.userToShortDto(event.getInitiator()));
        eventFullDto.setLocation(LocationMapper.toDto(event.getLocation()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getEventState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }


}
