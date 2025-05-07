package ewm.requests.controller;

import ewm.requests.dto.RequestDto;
import ewm.requests.service.RequestService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto postRequest(@PathVariable @NotNull @Positive Long userId,
                                  @RequestParam(required = true) @Positive Long eventId) {
        return requestService.postRequest(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getRequest(@PathVariable @NotNull @Positive Long userId) {
        return requestService.getRequest(userId);
    }

    @PatchMapping(path = "/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable @NotNull @Positive Long userId,
                                    @PathVariable @NotNull @Positive Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

}
