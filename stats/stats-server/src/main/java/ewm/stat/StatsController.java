package ewm.stat;


import ewm.exceptions.BadRequestException;
import ewm.stat.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import statdto.StatDtoRequest;
import statdto.StatDtoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody StatDtoRequest dtoRequest) {
        statService.addHit(dtoRequest);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatDtoResponse> getStats(@RequestParam(required = true) String start,
                                          @RequestParam(required = true) String end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startLdt = null;
        LocalDateTime endLdt = null;
        if (start != null && !start.isBlank()) {
            startLdt = LocalDateTime.parse(start, formatter);
        }
        if (end != null && !end.isBlank()) {
            endLdt = LocalDateTime.parse(end, formatter);
        }
        if ((end != null && !end.isBlank()) && (start != null && !start.isBlank()) && endLdt.isBefore(startLdt)) {
            throw new BadRequestException("дата завершения периода " + end + " раньше даты начала периода " + start);
        }
    return statService.getStats(start, end, uris, unique);
    }
}
