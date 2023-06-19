package com.chocolatecake.viewmodel.movieDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.chocolatecake.bases.BaseViewModel
import com.chocolatecake.entities.movieDetails.MovieDetailsEntity
import com.chocolatecake.entities.movieDetails.RatingEntity
import com.chocolatecake.entities.myList.FavoriteBodyRequestEntity

import com.chocolatecake.usecase.movie_details.GetMovieDetailsUseCase
import com.chocolatecake.usecase.movie_details.GetRatingUseCase
import com.chocolatecake.usecase.myList.MakeAsFavoriteUseCase
import com.chocolatecake.viewmodel.movieDetails.mapper.FavoriteBodyUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsUseCase: GetMovieDetailsUseCase,
    private val ratingUseCase: GetRatingUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val makeAsFavoriteUseCase: MakeAsFavoriteUseCase,
    private val favoriteBodyUiMapper: FavoriteBodyUiMapper,

    ) : BaseViewModel<MovieDetailsUiState, MovieDetailsUiEvent>(MovieDetailsUiState()),
    MovieDetailsListener {

    private val movieId = savedStateHandle.get<Int>("movieId") ?: 502356

    init {
        _state.update { it.copy(isLoading = true) }
        getMovieDetails(movieId)
    }


    private fun getMovieDetails(movieId: Int) {
        tryToExecute(
            call = { movieDetailsUseCase(movieId) },
            onSuccess = ::onSuccessMovieDetails,
            onError = ::onError
        )
    }

    private fun onError(th: Throwable) {
        val errors = _state.value.onErrors.toMutableList()
        errors.add(th.message.toString())
        _state.update { it.copy(onErrors = errors, isLoading = false) }
    }

    private fun onSuccessMovieDetails(movieDetails: MovieDetailsEntity) {
        _state.update {
            it.copy(
                id = movieDetails.id,
                movieUiState = MovieDetailsItem.Upper(
                    UpperUiState(
                        id = movieDetails.id,
                        backdropPath = movieDetails.backdropPath,
                        genres = movieDetails.genres,
                        title = movieDetails.title,
                        overview = movieDetails.overview,
                        voteAverage = movieDetails.voteAverage?.toFloat()?.div(2f),
                        videos = movieDetails.videos?.results?.map { it.key!! },
                    )
                ),
                recommendedUiState = MovieDetailsItem.Recommended(
                    movieDetails.recommendations?.recommendedMovies?.map {
                        RecommendedMoviesUiState(
                            id = it?.id,
                            voteAverage = it?.voteAverage,
                            backdropPath = it?.backdropPath,
                        )
                    },
                ),
                reviewUiState = MovieDetailsItem.Reviews(
                    movieDetails.reviewEntities?.map {
                        ReviewUiState(
                            name = it.name,
                            avatar_path = it.avatar_path,
                            content = it.content,
                            created_at = it.created_at
                        )
                    }
                ),
                castUiState = MovieDetailsItem.People(
                    movieDetails.credits?.cast?.map {
                        CastUiState(
                            id = it?.id,
                            name = it?.name,
                            profilePath = it?.profilePath
                        )
                    }
                ),
                isLoading = false
            )
        }
    }

    fun onRatingSubmit(rating: Float, movieId: Int) {
        tryToExecute(
            call = { ratingUseCase(movieId, rating) },
            onSuccess = ::onSuccessRating,
            onError = ::onError
        )
    }

    private fun onSuccessRating(ratingEntity: RatingEntity) {
        //todo
        sendEvent(MovieDetailsUiEvent.onSuccessRateEvent(ratingEntity.statusMessage))

    }




    override fun onClickPeople(itemId: Int) {
        sendEvent(MovieDetailsUiEvent.PeopleEvent(itemId))
    }

    override fun onClickRecommendedMovie(itemId: Int) {
        sendEvent(MovieDetailsUiEvent.RecommendedMovieEvent(itemId))
    }

    override fun onClickPlayTrailer(keys: List<String>) {
        sendEvent(MovieDetailsUiEvent.PlayVideoEvent(keys))
    }


    override fun onClickRate(id: Int) {
        sendEvent(MovieDetailsUiEvent.RateMovieEvent(id))
    }

    override fun onClickBackButton() {
        sendEvent(MovieDetailsUiEvent.OnClickBack)
    }

    override fun onClickSaveButton(id: Int) {

        tryToExecute(
            call = {
                makeAsFavoriteUseCase.invoke(
                        FavoriteBodyRequestEntity(
                            true,
                            20,
                            "movie"
                    )
                )
            },
            onSuccess = { true },
            onError = ::onError
        )
    }


//        private fun getAccountDetails() {
//        viewModelScope.launch {
//            val profileEntity = profileUiMapper.map(getAccountDetailsUseCase())
//            _state.update {
//                it.copy(
//                    username = profileEntity.username,
//                    avatarUrl = profileEntity.avatarUrl,
//                    error = null,
//                    isLogout = false
//                )
//            }
//        }
//    }

//    private fun onSuccessAsFav() : Boolean {
//        //todo
//      return true
//
//    }
}