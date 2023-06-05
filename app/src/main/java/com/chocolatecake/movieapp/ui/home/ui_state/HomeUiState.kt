package com.chocolatecake.movieapp.ui.home.ui_state

import com.chocolatecake.movieapp.ui.home.HomeItem

data class HomeUiState(
    val upComingMovies: HomeItem = HomeItem.Slider(emptyList()),
    val nowPlayingMovies: HomeItem = HomeItem.NowPlaying(emptyList()),
    val trendingMovies: HomeItem = HomeItem.Trending(emptyList()),
    val popularPeople: HomeItem = HomeItem.PopularPeople(emptyList()),
    val popularMovies: List<PopularMoviesUiState> = emptyList(),
    val topRated: List<TopRatedUiState> = emptyList(),
    val recommended: List<RecommendedUiState> = emptyList(),
    val onErrors: List<Error> = emptyList(),
    val isLoading: Boolean = false,
)

data class PopularPeopleUiState(
    val id: Int,
    val profilePath: String,
    val name: String
)

data class UpComingMoviesUiState(
    val id: Int,
    val imageUrl: String,
)

data class NowPlayingUiState(
    val id: Int,
    val imageUrl: String,
)

data class TrendingMoviesUiState(
    val id: Int,
    val imageUrl: String,
    val rate: Double
)

data class PopularMoviesUiState(
    val id: Int,
    val imageUrl: String,
    val rate: Double
)

data class RecommendedUiState(
    val id: Int,
    val imageUrl: String,
    val rate: Double
)

data class TopRatedUiState(
    val id: Int,
    val imageUrl: String,
    val rate: Double
)
