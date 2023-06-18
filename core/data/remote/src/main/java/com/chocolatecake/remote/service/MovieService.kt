package com.chocolatecake.remote.service

import com.chocolatecake.remote.request.LoginRequest
import com.chocolatecake.remote.request.RateRequest
import com.chocolatecake.remote.response.DataWrapperResponse
import com.chocolatecake.remote.response.GenresWrapperResponse
import com.chocolatecake.remote.response.auth.RequestTokenResponse
import com.chocolatecake.remote.response.auth.SessionResponse
import com.chocolatecake.remote.response.dto.GenreMovieRemoteDto
import com.chocolatecake.remote.response.dto.MovieRemoteDto
import com.chocolatecake.remote.response.dto.PeopleRemoteDto
import com.chocolatecake.remote.response.dto.TvRatingRemoteDto
import com.chocolatecake.remote.response.dto.TvDetailsCreditRemoteDto
import com.chocolatecake.remote.response.dto.TvDetailsRemoteDto
import com.chocolatecake.remote.response.dto.TvReviewRemoteDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    /// region auth
    @FormUrlEncoded
    @POST("authentication/session/new")
    suspend fun createSession(@Field("request_token") requestToken: String): Response<SessionResponse>

    @GET("authentication/token/new")
    suspend fun createRequestToken(): Response<RequestTokenResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<RequestTokenResponse>
    /// endregion

    /// region movie
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("page") page: Int = 1): Response<DataWrapperResponse<MovieRemoteDto>>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): Response<DataWrapperResponse<MovieRemoteDto>>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int = 1): Response<DataWrapperResponse<MovieRemoteDto>>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int = 1): Response<DataWrapperResponse<MovieRemoteDto>>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendedMovies(
        @Query("page") page: Int = 1,
        @Path("movie_id") movieId: Int
    ): Response<DataWrapperResponse<MovieRemoteDto>>

    @GET("movie/latest")
    suspend fun getLatestMovie(): Response<MovieRemoteDto>

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String = "day"
    ): Response<DataWrapperResponse<MovieRemoteDto>>
    ///endregion

    /// region search
    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("query") query: String,
        @Query("year") year: Int? = null,
        @Query("primary_release_year") primaryReleaseYear: Int? = null,
        @Query("region") region: String? = null,
        @Query("page") page: Int = 1,
    ): Response<DataWrapperResponse<MovieRemoteDto>>

    /// endregion

    /// region popular people
    @GET("person/popular")
    suspend fun getPopularPeople(@Query("page") page: Int = 1): Response<DataWrapperResponse<PeopleRemoteDto>>
    /// endregion

    /// region genres
    @GET("genre/movie/list")
    suspend fun getListOfGenresForMovies(
        @Query("language") language: String = "en"
    ): Response<GenresWrapperResponse<GenreMovieRemoteDto>>
    ///endregion

    /// region tv
    @GET("tv/{tv_id}")
    suspend fun getTvDetails(
        @Path("tv_id") tvShowId: Int
    ): Response<TvDetailsRemoteDto>

    @GET("tv/{tv_id}/aggregate_credits")
    suspend fun getTvDetailsCredit(
        @Path("tv_id") tvShowId: Int
    ): Response<TvDetailsCreditRemoteDto>

    @POST("tv/{tv_id}/rating?")
    suspend fun rateTvShow(
        @Body rateRequest: RateRequest, @Path("tv_id") tvShowId: Int,
    ):Response<TvRatingRemoteDto>

    @GET("tv/{tv_id}/reviews")
    suspend fun getTvShowReviews(
        @Path("tv_id") tvShowId: Int
    ):Response<GenresWrapperResponse<TvReviewRemoteDto>>

    @GET("tv/{tv_id}/recommendations")
    suspend fun getTvShowRecomendations(
        @Path("tv_id") tvShowId: Int
    )
    /// endregion
}