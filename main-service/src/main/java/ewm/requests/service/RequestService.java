package ewm.requests.service;

import ewm.requests.dto.RequestDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface RequestService {
    RequestDto postRequest(Long userId, Long eventId);

    List<RequestDto> getRequest(Long userId);

    List<RequestDto> getRequestByIds(List<Long> requestIds);

    List<RequestDto> patchRequestByIds(List<RequestDto> requestDtoList);

    List<RequestDto> getRequestByEventId(Long eventId);

    RequestDto cancelRequest(@NotNull @Positive Long userId, @NotNull @Positive Long requestId);
}
