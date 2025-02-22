package com.movies.movie.controller;

import com.movies.movie.dto.MovieDTO;
import com.movies.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {
    @Autowired
    private final MovieService service;


    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getById(@PathVariable String id) {
        try {
            MovieDTO movieDTO = service.getOne(id);
            return ResponseEntity.ok(movieDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createMovie(@RequestBody MovieDTO body) {
        MovieDTO response = service.createNewMovie(body);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable String id, @RequestBody MovieDTO body) {
        MovieDTO response = service.updateMovie(id, body);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<MovieDTO> getAllMovies(
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return service.findAll(page, size);
    }

}
