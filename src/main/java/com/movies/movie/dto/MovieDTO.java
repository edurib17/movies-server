package com.movies.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private String id;
    private String title;
    private String genre;
    private Integer releaseYear;
    private List<CastDTO> castList;
}
