package ewm.stat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "HITS", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "APP", nullable = false)
    private String app;

    @Column(name = "URI")
    private String uri;

    @Column(name = "IP", nullable = false)
    private String ip;

    @Column(name = "HITDATETIME", nullable = false)
    private LocalDateTime hitDateTime;
}
