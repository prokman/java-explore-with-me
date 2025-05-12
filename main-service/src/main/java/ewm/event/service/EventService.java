package ewm.event.service;

import ewm.event.dto.*;
import ewm.requests.dto.RequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface EventService {
    EventFullDto postEvent(@Valid @NotNull NewEventDto request, @NotNull @Positive Long userId);

    EventFullDto patchEvent(@Valid @NotNull UpdateEventUserRequest request, @NotNull @Positive Long userId, @NotNull @Positive Long eventId);

    List<EventShortDto> getEvents(@NotNull @Positive Long userId, Integer from, Integer size);

    EventFullDto getEventsById(@NotNull @Positive Long userId, @NotNull @Positive Long eventId);

    EventRequestStatusUpdateResult patchRequest(@NotNull @Positive Long userId, @NotNull @Positive Long eventId, @Valid @NotNull EventRequestStatusUpdateRequest request);

    List<RequestDto> getEventRequests(@NotNull @Positive Long userId, @NotNull @Positive Long eventId);
}
