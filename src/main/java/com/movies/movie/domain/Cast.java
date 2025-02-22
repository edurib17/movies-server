package com.movies.movie.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cast")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cast {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonBackReference
    private Movie movie;

    @Column(nullable = false)
    private String actorName;

    @Column(nullable = false)
    private String characterName;
}