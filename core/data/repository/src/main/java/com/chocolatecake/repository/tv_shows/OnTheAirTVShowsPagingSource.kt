package com.chocolatecake.repository.tv_shows

import android.util.Log
import com.chocolatecake.entities.TVShowsEntity
import com.chocolatecake.remote.response.dto.TVShowsRemoteDto
import com.chocolatecake.remote.service.MovieService
import com.chocolatecake.repository.BasePagingSource
import com.chocolatecake.repository.mappers.domain.tv.TVShowsDomainMapper
import javax.inject.Inject

class OnTheAirTVShowsPagingSource @Inject constructor(
    service: MovieService,
    private val mapper: TVShowsDomainMapper
) :
    BasePagingSource<TVShowsEntity>(service) {

    override suspend fun fetchData(page: Int): List<TVShowsEntity> {

        val response = service.getOnTheAirTVShows(page).body()?.results?.filterNotNull()
        val tvShowsEntities = response?.map { mapper.map(it) } ?: emptyList()

        val test = service.getOnTheAirTVShows(page).isSuccessful
        Log.d("source--OnTheAir", "$test")

        return tvShowsEntities
    }
}