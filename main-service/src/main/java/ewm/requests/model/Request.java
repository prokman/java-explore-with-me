package ewm.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.event.model.Event;
import ewm.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PARTICIPATION_REQUESTS", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    public Request(Long eventId, Long requesterId) {
        this.created = LocalDateTime.now();
        this.eventId = eventId;
        this.requesterId = requesterId;
        this.requestStatus = RequestStatus.PENDING;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATED")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @Column(name = "EVENT_ID")
    Long eventId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "EVENT_ID", insertable = false, updatable = false)
        Event event;

    @Column(name = "REQUESTER_ID")
    Long requesterId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "REQUESTER_ID", insertable = false, updatable = false)
        User requester;

    @Enumerated(EnumType.STRING)
    RequestStatus requestStatus;
}
