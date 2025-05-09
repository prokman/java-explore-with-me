package ewm.requests.service;

import ewm.event.model.Event;
import ewm.event.model.EventState;
import ewm.event.repository.EventRepository;
import ewm.exceptions.ConditionNotMeetException;
import ewm.exceptions.NotFoundException;
import ewm.requests.dto.RequestDto;
import ewm.requests.dto.RequestMapper;
import ewm.requests.model.Request;
import ewm.requests.model.RequestStatus;
import ewm.requests.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImp implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public RequestDto postRequest(Long userId, Long eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConditionNotMeetException("Запрос от пользователя " + userId + " на событие " + eventId + " уже создан");
        }

        Request request = new Request(eventId, userId);
        Event event = eventRepository.findWithDetailsById(eventId)
                .orElseThrow(() -> new NotFoundException("event с ИД" + eventId + " не найдено"));
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConditionNotMeetException("пользователя " + userId + " является инициатором события " + eventId);
        }

        if (event.getEventState().equals(EventState.PENDING) || event.getEventState().equals(EventState.CANCELED)) {
            throw new ConditionNotMeetException("Добавление запроса невозможно.Событие " + eventId + " имеет статус " + event.getEventState());
        }

        Long participantLimit = (event.getParticipantLimit() != null) ? event.getParticipantLimit().longValue() : null;
        Long confirmedRequests = event.getConfirmedRequests();
        if ((confirmedRequests.equals(participantLimit)) && (participantLimit != 0)) {
            throw new ConditionNotMeetException("Мест нет. Лимит " + event.getParticipantLimit() +
                    " заявлено " + event.getConfirmedRequests());
        }

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setRequestStatus(RequestStatus.CONFIRMED);
            Long confReq = event.getConfirmedRequests();
            event.setConfirmedRequests(++confReq);
            eventRepository.save(event);
        }

        return RequestMapper.requestToDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getRequest(Long userId) {
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(RequestMapper::requestToDto)
                .toList();
    }

    @Override
    public List<RequestDto> getRequestByIds(List<Long> requestIds) {
        return requestRepository
                .findAllById(requestIds).stream().map(RequestMapper::requestToDto).toList();
    }

    @Override
    @Transactional
    public List<RequestDto> patchRequestByIds(List<RequestDto> requestDtoList) {
        List<Request> requestList = requestDtoList.stream().map(RequestMapper::dtoToRequest).toList();
        List<Request> savedList = requestRepository.saveAll(requestList);
        return savedList.stream().map(RequestMapper::requestToDto).toList();
    }

    @Override
    public List<RequestDto> getRequestByEventId(Long eventId) {
        return requestRepository.findAllByEventId(eventId).stream().map(RequestMapper::requestToDto).toList();
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("request №" + requestId + " не найден"));
        request.setRequestStatus(RequestStatus.CANCELED);
        return RequestMapper.requestToDto(requestRepository.save(request));
    }


}
