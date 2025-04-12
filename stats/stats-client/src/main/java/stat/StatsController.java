package stat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import statdto.StatDtoRequest;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@Valid @RequestBody StatDtoRequest dtoRequest) {
        statsClient.addHit(dtoRequest);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getStats(@RequestParam(required = true) String start,
                                           @RequestParam(required = true) String end,
                                           @RequestParam(required = true) List<String> uris,
                                           @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        if (LocalDateTime.parse(end).isBefore(LocalDateTime.parse(start))) {
            throw new RuntimeException("End must be after start");
        }
        return statsClient.getStats(start, end, uris, unique);
    }
}
