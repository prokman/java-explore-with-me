package ewm.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ewm.categories.model.Category;
import ewm.compilation.model.Compilation;
import ewm.location.model.Location;
import ewm.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 20, max = 2000, message = "name должно быть не короче 20 символов и не больше 2000")
    @Column(name = "ANNOTATION", nullable = false)
    private String annotation;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "CATEGORY_ID", nullable = false)
        private Category category;

    @NotNull(message = "confirmedRequests не может быть NULL")
    @Min(value = 0, message = "confirmedRequests не может быть отрицательным")
    private Long confirmedRequests;

    @Column(name = "created_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotBlank
    @Size(min = 20, max = 7000, message = "name должно быть не короче 20 символов и не больше 7000")
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotNull(message = "description не может быть NULL")
    @Column(name = "EVENT_DATE", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "INITIATOR_ID", nullable = false)
        private User initiator;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "LOCATION_ID")
        private Location location;

    @Column(name = "PAID", nullable = false)
    private Boolean paid;

    @Min(value = 0, message = "participantLimit не может быть отрицательным")
    @Column(name = "PARTICIPANT_LIMIT")
    private Integer participantLimit;

    @Column(name = "PUBLISHED_ON")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState eventState;

    @NotNull(message = "title не может быть NULL")
    @Size(min = 3, max = 120, message = "title должно быть не короче 3 символов и не больше 120")
    @Column(name = "TITLE")
    private String title;

    @Transient
    private Long views;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "events")
    private Set<Compilation> compilations;
}