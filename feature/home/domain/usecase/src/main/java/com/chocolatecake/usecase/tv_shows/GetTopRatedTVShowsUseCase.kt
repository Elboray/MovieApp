package com.chocolatecake.usecase.tv_shows

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.filter
import com.chocolatecake.entities.TVShowsEntity
import com.chocolatecake.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTopRatedTVShowsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genreId: Int? = null): Flow<PagingData<TVShowsEntity>> {
        Log.d("list---UseCase---TopRated", movieRepository.getTopRatedTVShows().toString())
        return movieRepository.getTopRatedTVShows().flow.map {
            it.filter { tvShow ->
                tvShow.genreEntities.takeIf { genreId != null }
                    ?.map { it.genreID }
                    ?.contains(genreId) ?: true && tvShow.rate != 0.0
            }
        }
    }
}