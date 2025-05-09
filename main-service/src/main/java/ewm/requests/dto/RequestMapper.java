package ewm.requests.dto;

import ewm.requests.model.Request;

public class RequestMapper {
    public static Request dtoToRequest(RequestDto requestDto) {
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setCreated(requestDto.getCreated());
        request.setEventId(requestDto.getEvent());
        request.setRequesterId(requestDto.getRequester());
        request.setRequestStatus(requestDto.getStatus());
        return request;
    }

    public static RequestDto requestToDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEventId());
        requestDto.setRequester(request.getRequesterId());
        requestDto.setStatus(request.getRequestStatus());
        return requestDto;
    }
}
