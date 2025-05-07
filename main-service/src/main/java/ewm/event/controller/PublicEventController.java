package ewm.event.controller;

import ewm.event.dto.EventFullDto;
import ewm.event.dto.GetPublicEventsParam;
import ewm.event.service.PublicEventService;
import ewm.exceptions.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import stat.StatsClient;
import statdto.StatDtoRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final PublicEventService publicEventService;
    private final StatsClient statsClient = new StatsClient();

    @GetMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getPublicEventById(@PathVariable @NotNull @Positive Long eventId,
                                           HttpServletRequest httpServletRequest) {
        EventFullDto eventFullDto = publicEventService.getPublicEventById(eventId);

        String app = PublicEventService.class.getPackageName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StatDtoRequest statDtoRequest = new StatDtoRequest(app, httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(), formatter.format(LocalDateTime.now()));
        statsClient.addHit(statDtoRequest);
        return eventFullDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllPublicEventByParam(@RequestParam(required = false) String text,
                                                       @RequestParam(required = false) List<Integer> categories,
                                                       @RequestParam(required = false) Boolean paid,
                                                       @RequestParam(required = false) String rangeStart,
                                                       @RequestParam(required = false) String rangeEnd,
                                                       @RequestParam(required = false) Boolean onlyAvailable,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false, defaultValue = "0") Integer from,
                                                       @RequestParam(required = false, defaultValue = "10") Integer size,
                                                       HttpServletRequest httpServletRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null && !rangeStart.isBlank()) {
            start = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null && !rangeEnd.isBlank()) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        if ((rangeEnd != null && !rangeEnd.isBlank()) && (rangeStart != null && !rangeStart.isBlank()) && end.isBefore(start)) {
            throw new BadRequestException("дата завершения периода " + end + " раньше даты начала периода " + start);
        }
        GetPublicEventsParam param = new GetPublicEventsParam(text, categories, paid, onlyAvailable, sort, start, end);
        List<EventFullDto> eventFullDtoList = publicEventService.getAllPublicEventByParam(param, from, size);

        String app = PublicEventService.class.getPackageName();

        StatDtoRequest statDtoRequest = new StatDtoRequest(app, httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(), formatter.format(LocalDateTime.now()));
        statsClient.addHit(statDtoRequest);
        return eventFullDtoList;
    }


}
