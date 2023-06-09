package com.chocolatecake.movieapp.domain.usecases.home

import com.chocolatecake.movieapp.domain.model.movie.Movie
import com.chocolatecake.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetNowPlayingUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(limit: Int = 10): List<Movie> {
        return movieRepository.getNowPlayingMovies().take(limit)
    }

}