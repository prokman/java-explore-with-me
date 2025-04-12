package ewm.stat;


import ewm.stat.service.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import statdto.StatDtoRequest;
import statdto.StatDtoResponse;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
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
                                          @RequestParam(required = true) List<String> uris,
                                          @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statService.getStats(start, end, uris, unique);
    }
}
