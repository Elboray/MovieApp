package com.chocolatecake.movieapp.domain.usecases.home

import com.chocolatecake.movieapp.data.local.database.entity.movie.UpcomingMovieEntity
import com.chocolatecake.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Random
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): Flow<List<UpcomingMovieEntity>> {
        return movieRepository.getUpcomingMovies().map { it.shuffled(Random()) }
    }
}