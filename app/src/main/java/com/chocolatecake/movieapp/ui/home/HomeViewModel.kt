package com.chocolatecake.movieapp.ui.home

import com.chocolatecake.movieapp.data.local.database.dto.PopularPeopleLocalDto
import com.chocolatecake.movieapp.data.local.database.dto.movie.NowPlayingMovieLocalDto
import com.chocolatecake.movieapp.data.local.database.dto.movie.PopularMovieLocalDto
import com.chocolatecake.movieapp.data.local.database.dto.movie.TopRatedMovieLocalDto
import com.chocolatecake.movieapp.data.local.database.dto.movie.TrendingMoviesLocalDto
import com.chocolatecake.movieapp.data.local.database.dto.movie.UpcomingMovieLocalDto
import com.chocolatecake.movieapp.domain.usecases.home.GetNowPlayingUseCase
import com.chocolatecake.movieapp.domain.usecases.home.GetPopularMoviesUseCase
import com.chocolatecake.movieapp.domain.usecases.home.GetPopularPeopleUseCase
import com.chocolatecake.movieapp.domain.usecases.home.GetTopRatedUseCase
import com.chocolatecake.movieapp.domain.usecases.home.GetTrendingMoviesUseCase
import com.chocolatecake.movieapp.domain.usecases.home.GetUpcomingMoviesUseCase
import com.chocolatecake.movieapp.home.adapter.HomeListener
import com.chocolatecake.movieapp.ui.base.BaseViewModel
import com.chocolatecake.movieapp.ui.home.ui_state.HomeUiEvent
import com.chocolatecake.movieapp.ui.home.ui_state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nowPlayingUseCase: GetNowPlayingUseCase,
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val popularPeopleUseCase: GetPopularPeopleUseCase,
    private val topRatedUseCase: GetTopRatedUseCase,
    private val trendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val upcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val upComingUiMapper: UpComingUiMapper,
    private val nowPlayingDomainMapper: NowPlayingDomainMapper,
    private val trendingUiMapper: TrendingUiMapper,
    private val topRatedUiMapper: TopRatedUiMapper,
    private val popularPeopleUiMapper: PopularPeopleUiMapper,
    private val popularMoviesUiMapper: PopularMoviesUiMapper,
) : BaseViewModel<HomeUiState, HomeUiEvent>(), HomeListener {

    override fun initialState() = HomeUiState()

    init {
        getData()
    }

    override fun getData() {
        _state.update { it.copy(isLoading = true) }
        getUpComingMovies()
        getPopularPeople()
        getNowPlayingMovies()
        getTrendingMovies()
        getPopularMovies()
        getTopRatedMovies()

    }

    private fun getPopularMovies() {
        tryToExecuteFlow(
            call = { popularMoviesUseCase() },
            onSuccess = ::onSuccessPopularMovies,
            onError = ::onError
        )
    }

    private fun onSuccessPopularMovies(popularMovieEntities: List<PopularMovieLocalDto>) {
        val items = popularMovieEntities.map(popularMoviesUiMapper::map)
        _state.update {
            it.copy(
                popularMovies = HomeItem.PopularMovies(items), isLoading = false
            )
        }

    }

    private fun getTopRatedMovies() {
        tryToExecuteFlow(
            call = { topRatedUseCase() },
            onSuccess = ::onSuccessTopRatedMovies,
            onError = ::onError
        )
    }

    private fun onSuccessTopRatedMovies(topRatedMovieEntities: List<TopRatedMovieLocalDto>) {
        val items = topRatedMovieEntities.map(topRatedUiMapper::map)
        _state.update {
            it.copy(
                topRated = HomeItem.TopRated(items), isLoading = false
            )
        }
    }


    private fun getUpComingMovies() {
        tryToExecuteFlow(
            call = {upcomingMoviesUseCase()},
            onSuccess = ::onSuccessUpcomingMovies,
            onError = ::onError
        )
    }

    private fun onSuccessUpcomingMovies(upcomingMovieEntities: List<UpcomingMovieLocalDto>) {
        val items = upcomingMovieEntities.map(upComingUiMapper::map)
        _state.update {
            it.copy(
                upComingMovies = HomeItem.Slider(items), isLoading = false
            )
        }
    }

    private fun getPopularPeople() {
        tryToExecuteFlow(
            call = {popularPeopleUseCase()},
            onSuccess = ::onSuccessPopularPeople,
            onError = ::onError
        )
    }

    private fun onSuccessPopularPeople(popularPeopleEntities: List<PopularPeopleLocalDto>) {
        val items = popularPeopleEntities.map(popularPeopleUiMapper::map)
        _state.update {
            it.copy(
                popularPeople = HomeItem.PopularPeople(items), isLoading = false
            )
        }
    }

    private fun getNowPlayingMovies() {
        tryToExecuteFlow(
            call = {nowPlayingUseCase()},
            onSuccess = ::onSuccessNowPlayingMovies,
            onError = ::onError
        )
    }

    private fun onSuccessNowPlayingMovies(nowPlayingMovieEntities: List<NowPlayingMovieLocalDto>) {
        val items = nowPlayingMovieEntities.map(nowPlayingDomainMapper::map)
        _state.update {
            it.copy(
                nowPlayingMovies = HomeItem.NowPlaying(items), isLoading = false
            )
        }
    }

    private fun getTrendingMovies() {
        tryToExecuteFlow(
            call = {trendingMoviesUseCase()},
            onSuccess = ::onSuccessTrendingMovies,
            onError = ::onError
        )
    }

    private fun onSuccessTrendingMovies(trendingMoviesEntities: List<TrendingMoviesLocalDto>) {
        val items = trendingMoviesEntities.map(trendingUiMapper::map)
        _state.update {
            it.copy(
                trendingMovies = HomeItem.Trending(items), isLoading = false
            )
        }
    }

    private fun onError(th: Throwable) {
        val errors = _state.value.onErrors.toMutableList()
        errors.add(th.message.toString())
        _state.update { it.copy(onErrors = errors, isLoading = false) }
    }

    override fun onClickNowPlaying(itemId: Int) {
        sendEvent(HomeUiEvent.NowPlayingMovieEvent(itemId))
    }

    override fun onClickTrending(itemId: Int) {
        sendEvent(HomeUiEvent.TrendingMovieEvent(itemId))
    }

    override fun onClickPopularMovies(itemId: Int) {
        sendEvent(HomeUiEvent.PopularMovieEvent(itemId))
    }

    override fun onClickTopRated(itemId: Int) {
        sendEvent(HomeUiEvent.TopRatedMovieEvent(itemId))
    }


    override fun onClickUpComing(itemId: Int) {
        sendEvent(HomeUiEvent.UpComingMovieEvent(itemId))
    }

    override fun onClickPopularPeople(itemId: Int) {
        sendEvent(HomeUiEvent.PopularPeopleEvent(itemId))
    }

}