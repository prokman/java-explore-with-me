package ewm.compilation.model;

import ewm.event.model.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "compilations", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PINNED", nullable = false)
    private Boolean pinned;

    @Column(name = "TITLE", nullable = false)
    private String title;

        @ManyToMany
        @JoinTable(
                name = "COMPILATIONS_EVENTS",
                schema = "public",
                joinColumns = @JoinColumn(name = "COMPILATIONS_ID", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "EVENT_ID", referencedColumnName = "id")
        )
        private Set<Event> events;
}