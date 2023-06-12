package com.chocolatecake.movieapp.domain.usecases.genres


import com.chocolatecake.movieapp.domain.entities.GenreEntity
import com.chocolatecake.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetAllGenresMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(): List<GenreEntity> {
        return movieRepository.getGenresMovies().sortedBy { it.genreName }
    }
}