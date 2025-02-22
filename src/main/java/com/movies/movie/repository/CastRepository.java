package com.movies.movie.repository;

import com.movies.movie.domain.Cast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastRepository extends JpaRepository<Cast, String> {
}
