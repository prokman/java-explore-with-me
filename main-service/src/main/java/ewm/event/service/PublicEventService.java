package ewm.event.service;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.EventShortDto;
import ewm.event.dto.GetPublicEventsParam;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Set;

public interface PublicEventService {
    EventFullDto getPublicEventById(@NotNull @Positive Long eventId);

    List<EventFullDto> getAllPublicEventByParam(GetPublicEventsParam param, Integer from, Integer size);

    Set<EventShortDto> getShrotDtoEventsSet(Set<Long> eventIds);
}
