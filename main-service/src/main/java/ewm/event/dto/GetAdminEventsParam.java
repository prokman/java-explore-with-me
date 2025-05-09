package ewm.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.event.model.EventState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAdminEventsParam {
    private List<Integer> users;
    private List<EventState> states;
    private List<Integer> categories;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

}
