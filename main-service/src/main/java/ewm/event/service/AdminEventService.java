package ewm.event.service;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.GetAdminEventsParam;
import ewm.event.dto.UpdateEventAdminRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getAdminEvents(GetAdminEventsParam param, Integer from, Integer size);

    EventFullDto patchAdminEvents(@Valid @NotNull UpdateEventAdminRequest request, @NotNull @Positive Long eventId);
}
