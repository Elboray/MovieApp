package com.chocolatecake.movieapp.data.mappers.search

import com.chocolatecake.movieapp.BuildConfig
import com.chocolatecake.movieapp.data.mappers.GenreMapper
import com.chocolatecake.movieapp.data.remote.response.MovieDto
import com.chocolatecake.movieapp.data.mappers.Mapper
import com.chocolatecake.movieapp.domain.entities.MovieEntity
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MovieUIMapper @Inject constructor(
    private val genreMapper: GenreMapper
): Mapper<MovieDto, MovieEntity> {
    override fun map(input: MovieDto): MovieEntity {
        return MovieEntity(
            id = input.id ?: 0,
            rate = input.voteAverage ?: 0.0,
            title = input.title ?: "",
            genreEntities = genreMapper.map(input.genreIds),
            imageUrl = BuildConfig.IMAGE_BASE_PATH + input.posterPath,
        )
    }
}