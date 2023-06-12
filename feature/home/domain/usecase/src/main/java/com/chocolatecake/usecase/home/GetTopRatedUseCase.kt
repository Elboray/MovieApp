package com.chocolatecake.usecase.home

import com.chocolatecake.entities.MovieEntity
import com.chocolatecake.repository.MovieRepository
import javax.inject.Inject

class GetTopRatedUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(limit: Int = 10): List<MovieEntity> {
        return movieRepository.getTopRatedMovies().take(limit)
    }
}