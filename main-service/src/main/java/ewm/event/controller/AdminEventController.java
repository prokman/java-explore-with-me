package ewm.event.controller;

import ewm.event.dto.*;
import ewm.event.model.EventState;
import ewm.event.service.AdminEventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAdminEvents(@RequestParam(required = false) List<Integer> users,
                                             @RequestParam(required = false) List<EventState> states,
                                             @RequestParam(required = false) List<Integer> categories,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null && !rangeStart.isBlank()) {
            start = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null && !rangeEnd.isBlank()) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        GetAdminEventsParam param = new GetAdminEventsParam(users, states, categories, start, end);
        return adminEventService.getAdminEvents(param, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto patchEvent(@PathVariable @NotNull @Positive Long eventId,
                                   @RequestBody @Valid @NotNull UpdateEventAdminRequest request) {
        return adminEventService.patchAdminEvents(request, eventId);
    }
}
