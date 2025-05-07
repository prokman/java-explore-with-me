package ewm.event.controller;

import ewm.event.dto.*;
import ewm.event.service.EventService;
import ewm.requests.dto.RequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable @NotNull @Positive Long userId,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@RequestBody @Valid @NotNull NewEventDto request,
                                  @PathVariable @NotNull @Positive Long userId) {
        return eventService.postEvent(request, userId);
    }

    @GetMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventsById(@PathVariable @NotNull @Positive Long userId,
                                      @PathVariable @NotNull @Positive Long eventId) {
        return eventService.getEventsById(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto patchEvent(@RequestBody @Valid @NotNull UpdateEventUserRequest request,
                                   @PathVariable @NotNull @Positive Long userId,
                                   @PathVariable @NotNull @Positive Long eventId) {
        return eventService.patchEvent(request, userId, eventId);
    }

    @GetMapping(path = "/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getEventRequests(@PathVariable @NotNull @Positive Long userId,
                                             @PathVariable @NotNull @Positive Long eventId) {
        //тут получаем парам для поиска и выдачи лист реквестов
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult patchRequest(@PathVariable @NotNull @Positive Long userId,
                                     @PathVariable @NotNull @Positive Long eventId,
                                     @RequestBody @Valid @NotNull EventRequestStatusUpdateRequest request) {
        return eventService.patchRequest(userId, eventId, request);
    }


}
