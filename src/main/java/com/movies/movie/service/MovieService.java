package com.movies.movie.service;

import com.movies.movie.domain.Cast;
import com.movies.movie.domain.Movie;
import com.movies.movie.dto.CastDTO;
import com.movies.movie.dto.MovieDTO;
import com.movies.movie.repository.CastRepository;
import com.movies.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {
    @Autowired
    private final MovieRepository repository;
    @Autowired
    private final CastRepository castRepository;
    @Autowired
    private ModelMapper modelMapper;

    private MovieDTO convertToDto(Movie movie) {
        return modelMapper.map(movie, MovieDTO.class);
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        return modelMapper.map(movieDTO, Movie.class);
    }

    private Cast converToEntity(CastDTO castDTO) {
        return modelMapper.map(castDTO, Cast.class);
    }

    private List<MovieDTO> convertToListEntity(List<Movie> movies) {
        return movies.stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public MovieDTO getOne(String id) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado com ID: " + id));
        return convertToDto(movie);
    }

    public MovieDTO createNewMovie(MovieDTO movieDTO) {
        Movie movieToSave = convertToEntity(movieDTO);
        if (!movieToSave.getCastList().isEmpty()) {
            movieToSave.getCastList().forEach(cast -> cast.setMovie(movieToSave));
        }
        Movie movieSaved = repository.save(movieToSave);
        return convertToDto(movieSaved);
    }

    @Transactional
    public MovieDTO updateMovie(String id, MovieDTO movieToUpdateDTO) {
        Movie existingMovie = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filme não encontrado com ID: " + id));

        updateMovieDetails(existingMovie, movieToUpdateDTO);
        updateCast(existingMovie, movieToUpdateDTO.getCastList());

        Movie movieSaved = repository.save(existingMovie);
        return convertToDto(movieSaved);
    }


    private void updateMovieDetails(Movie existingMovie, MovieDTO movieToUpdateDTO) {
        existingMovie.setTitle(movieToUpdateDTO.getTitle());
        existingMovie.setReleaseYear(movieToUpdateDTO.getReleaseYear());
        existingMovie.setGenre(movieToUpdateDTO.getGenre());
    }

    private void updateCast(Movie movie, List<CastDTO> updatedCastDTOs) {
        List<Cast> updatedCastList = updatedCastDTOs.stream()
                .map(this::converToEntity)
                .peek(cast -> cast.setMovie(movie))
                .toList();

        List<Cast> castToDelete = movie.getCastList().stream()
                .filter(cast -> updatedCastList.stream().filter(u -> u.getId() != null)
                        .noneMatch(updatedCast -> updatedCast.getId().equals(cast.getId())))
                .collect(Collectors.toList());

        castRepository.deleteAll(castToDelete);
        movie.getCastList().clear();
        movie.getCastList().addAll(updatedCastList);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Page<MovieDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviesPage = repository.findAll(pageable);

        List<MovieDTO> movieDTOs = moviesPage.getContent().stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(movieDTOs, pageable, moviesPage.getTotalElements());
    }


}
