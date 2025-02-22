package com.movies.movie.dto;

import com.movies.movie.domain.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CastDTO {
    private String id;
    private String actorName;
    private String characterName;
}
