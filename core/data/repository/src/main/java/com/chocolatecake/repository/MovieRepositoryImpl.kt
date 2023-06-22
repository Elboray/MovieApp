package com.chocolatecake.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.chocolatecake.entities.GenreEntity
import com.chocolatecake.entities.MovieEntity
import com.chocolatecake.entities.PeopleEntity
import com.chocolatecake.entities.TvEntity
import com.chocolatecake.local.PreferenceStorage
import com.chocolatecake.entities.movieDetails.MovieDetailsEntity
import com.chocolatecake.entities.movieDetails.RatingEntity
import com.chocolatecake.entities.TVShowsEntity
import com.chocolatecake.entities.myList.FavoriteBodyRequestEntity
import com.chocolatecake.entities.myList.ListCreatedEntity
import com.chocolatecake.entities.myList.ListEntity
import com.chocolatecake.entities.myList.ListMovieEntity
import com.chocolatecake.entities.myList.WatchlistRequestEntity
import com.chocolatecake.local.database.MovieDao
import com.chocolatecake.local.database.dto.SearchHistoryLocalDto
import com.chocolatecake.remote.request.RatingRequest
import com.chocolatecake.remote.service.MovieService
import com.chocolatecake.repository.mappers.cash.LocalGenresMovieMapper
import com.chocolatecake.repository.mappers.cash.LocalGenresTvMapper
import com.chocolatecake.repository.mappers.cash.LocalPopularPeopleMapper
import com.chocolatecake.repository.mappers.cash.movie.LocalNowPlayingMovieMapper
import com.chocolatecake.repository.mappers.cash.movie.LocalPopularMovieMapper
import com.chocolatecake.repository.mappers.cash.movie.LocalTopRatedMovieMapper
import com.chocolatecake.repository.mappers.cash.movie.LocalTrendingMoviesMapper
import com.chocolatecake.repository.mappers.cash.movie.LocalUpcomingMovieMapper
import com.chocolatecake.repository.mappers.cash.myList.LocalFavoriteMoviesMapper
import com.chocolatecake.repository.mappers.cash.myList.LocalListMapper
import com.chocolatecake.repository.mappers.cash.myList.LocalListMovieMapper
import com.chocolatecake.repository.mappers.cash.myList.LocalMoviesMapper
import com.chocolatecake.repository.mappers.cash.myList.LocalWatchlistMapper
import com.chocolatecake.repository.mappers.domain.DomainGenreMapper
import com.chocolatecake.repository.mappers.domain.DomainMovieDetailsMapper
import com.chocolatecake.repository.mappers.domain.DomainGenreTvMapper
import com.chocolatecake.repository.mappers.domain.DomainPeopleMapper
import com.chocolatecake.repository.mappers.domain.DomainRatingMapper
import com.chocolatecake.repository.mappers.domain.DomainPeopleRemoteMapper
import com.chocolatecake.repository.mappers.domain.movie.DomainNowPlayingMovieMapper
import com.chocolatecake.repository.mappers.domain.movie.DomainPopularMovieMapper
import com.chocolatecake.repository.mappers.domain.movie.DomainTopRatedMovieMapper
import com.chocolatecake.repository.mappers.domain.movie.DomainTrendingMoviesMapper
import com.chocolatecake.repository.mappers.domain.movie.DomainUpcomingMovieMapper
import com.chocolatecake.repository.mappers.domain.myList.DomainFavoriteMoviesMapper
import com.chocolatecake.repository.mappers.domain.myList.DomainListMapper
import com.chocolatecake.repository.mappers.domain.myList.DomainListMovieMapper
import com.chocolatecake.repository.mappers.domain.myList.DomainMovieItemListMapper
import com.chocolatecake.repository.mappers.domain.myList.DomainMovieMapper
import com.chocolatecake.repository.mappers.domain.myList.DomainWatchlistMapper
import com.chocolatecake.repository.mappers.remote.RemoteFavoriteBodyMapper
import com.chocolatecake.repository.mappers.remote.WatchlistRequestMapper
import com.chocolatecake.repository.tv_shows.AiringTodayTVShowsPagingSource
import com.chocolatecake.repository.tv_shows.OnTheAirTVShowsPagingSource
import com.chocolatecake.repository.tv_shows.PopularTVShowsPagingSource
import com.chocolatecake.repository.tv_shows.TopRatedTVShowsPagingSource
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao,
    private val airingTodayTvShowsPagingSource: AiringTodayTVShowsPagingSource,
    private val topRatedTvShowsPagingSource: TopRatedTVShowsPagingSource,
    private val onTheAirTVShowsPagingSource: OnTheAirTVShowsPagingSource,
    private val popularTVShowsPagingSource: PopularTVShowsPagingSource,
    private val preferenceStorage: PreferenceStorage,
    private val localGenresMovieMapper: LocalGenresMovieMapper,
    private val localGenresTvMapper: LocalGenresTvMapper,
    private val localPopularMovieMapper: LocalPopularMovieMapper,
    private val localPopularPeopleMapper: LocalPopularPeopleMapper,
    private val localNowPlayingMovieMapper: LocalNowPlayingMovieMapper,
    private val localTopRatedMovieMapper: LocalTopRatedMovieMapper,
    private val localTrendingMoviesMapper: LocalTrendingMoviesMapper,
    private val localUpcomingMovieMapper: LocalUpcomingMovieMapper,
    private val localFavoriteMoviesMapper: LocalFavoriteMoviesMapper,
    private val localWatchlistMapper: LocalWatchlistMapper,
    private val localListMovieMapper: LocalListMovieMapper,
    private val localListMapper: LocalListMapper,
    private val localMoviesMapper: LocalMoviesMapper,
    private val domainListMapper: DomainListMapper,
    private val domainFavoriteMoviesMapper: DomainFavoriteMoviesMapper,
    private val domainWatchlistMapper: DomainWatchlistMapper,
    private val domainPopularMovieMapper: DomainPopularMovieMapper,
    private val domainNowPlayingMovieMapper: DomainNowPlayingMovieMapper,
    private val domainTopRatedMovieMapper: DomainTopRatedMovieMapper,
    private val domainUpcomingMovieMapper: DomainUpcomingMovieMapper,
    private val domainTrendingMovieMapper: DomainTrendingMoviesMapper,
    private val domainPeopleMapper: DomainPeopleMapper,
    private val domainGenreMapper: DomainGenreMapper,
    private val domainMovieDetailsMapper: DomainMovieDetailsMapper,
    private val domainRatingMapper: DomainRatingMapper,
    private val domainGenreTvMapper: DomainGenreTvMapper,
    private val domainPeopleRemoteMapper: DomainPeopleRemoteMapper,
    private val domainMoviesMapper: DomainMovieMapper,
    private val domainMovieItemListMapper: DomainMovieItemListMapper,
    private val domainListMovieMapper: DomainListMovieMapper,
    private val favoriteMoviesMapper: RemoteFavoriteBodyMapper,
    private val watchlistMapper: WatchlistRequestMapper,

    ) : BaseRepository(), MovieRepository {

    /// region movies
    override suspend fun getPopularMovies(): List<MovieEntity> {
        return domainPopularMovieMapper.map(movieDao.getPopularMovies())
    }

    override suspend fun refreshPopularMovies() {
        refreshWrapper(
            movieService::getPopularMovies,
            localPopularMovieMapper::map,
            movieDao::insertPopularMovies
        )
    }

    override suspend fun getNowPlayingMovies(): List<MovieEntity> {
        return domainNowPlayingMovieMapper.map(movieDao.getNowPlayingMovies())
    }

    override suspend fun refreshNowPlayingMovies() {
        refreshWrapper(
            movieService::getNowPlayingMovies,
            localNowPlayingMovieMapper::map,
            movieDao::insertNowPlayingMovies
        )
    }

    override suspend fun getTopRatedMovies(): List<MovieEntity> {
        return domainTopRatedMovieMapper.map(movieDao.getTopRatedMovies())
    }

    override suspend fun refreshTopRatedMovies() {
        refreshWrapper(
            movieService::getTopRatedMovies,
            localTopRatedMovieMapper::map,
            movieDao::insertTopRatedMovies
        )
    }

    override suspend fun getUpcomingMovies(): List<MovieEntity> {
        return domainUpcomingMovieMapper.map(movieDao.getUpcomingMovies())
    }

    override suspend fun refreshUpcomingMovies() {
        refreshWrapper(
            movieService::getUpcomingMovies,
            localUpcomingMovieMapper::map,
            movieDao::insertUpcomingMovies
        )
    }

    override suspend fun getTrendingMovies(): List<MovieEntity> {
        return domainTrendingMovieMapper.map(movieDao.getTrendingMovies())
    }

    override suspend fun refreshTrendingMovies() {
        refreshWrapper(
            movieService::getTrendingMovies,
            localTrendingMoviesMapper::map,
            movieDao::insertTrendingMovies
        )
    }

    override suspend fun getPopularPeople(): List<PeopleEntity> {
        return domainPeopleMapper.map(movieDao.getPopularPeople())
    }

    override suspend fun refreshPopularPeople() {
        refreshWrapper(
            movieService::getPopularPeople,
            localPopularPeopleMapper::map,
            movieDao::insertPopularPeople
        )
    }
    /// endregion

    /// region search history
    override suspend fun getSearchHistory(keyword: String): List<String> {
        return movieDao.getSearchHistory("%${keyword}%").map { it.keyword }

    }

    override suspend fun insertSearchHistory(keyword: String) {
        return movieDao.insertSearchHistory(SearchHistoryLocalDto(keyword))
    }

    override suspend fun clearAllSearchHistory() {
        return movieDao.clearAllSearchHistory()
    }

    override suspend fun deleteSearchHistory(keyword: String) {
        movieDao.deleteSearchHistory(keyword)
    }
    /// endregion

    ///region search
    override suspend fun searchForMovies(keyword: String): List<MovieEntity> {
        val genresEntities = getGenresMovies()
        return wrapApiCall { movieService.searchForMovies(keyword) }.results
            ?.filterNotNull()?.let { movieDtos ->
                movieDtos.map { input ->
                    MovieEntity(
                        id = input.id ?: 0,
                        rate = input.voteAverage ?: 0.0,
                        title = input.title ?: "",
                        genreEntities = filterGenres(
                            input.genreIds?.filterNotNull() ?: emptyList(),
                            genresEntities
                        ),
                        imageUrl = BuildConfig.IMAGE_BASE_PATH + input.posterPath,
                        year = input.releaseDate ?: ""
                    )
                }
            } ?: emptyList()
    }

    override suspend fun searchForTv(keyword: String): List<TvEntity> {
        val genresTvEntities = getGenresTvs()
        return wrapApiCall { movieService.searchForTv(keyword) }.results
            ?.filterNotNull()?.let { tvDtos ->
                tvDtos.map { input ->
                    TvEntity(
                        id = input.id ?: 0,
                        name = input.name ?: "",
                        rate = input.voteAverage ?: 0.0,
                        imageUrl = BuildConfig.IMAGE_BASE_PATH + input.posterPath,
                        genreEntities = filterGenres(
                            input.genreIds?.filterNotNull() ?: emptyList(),
                            genresTvEntities
                        ),
                        year = input.firstAirDate ?: ""
                    )
                }
            } ?: emptyList()
    }

    override suspend fun searchForPeople(keyword: String): List<PeopleEntity> {
        return wrapApiCall { movieService.searchForPeople(keyword) }.results
            ?.filterNotNull()?.map {
                domainPeopleRemoteMapper.map(it)
            } ?: emptyList()

    }

    private fun filterGenres(
        genresIds: List<Int>,
        genresEntities: List<GenreEntity>
    ): List<GenreEntity> = genresEntities.filter { it.genreID in genresIds }
    //endregion

    /// region genres
    override suspend fun getGenresMovies(): List<GenreEntity> {
        return domainGenreMapper.map(movieDao.getGenresMovies())
    }

    override suspend fun refreshGenres() {
        try {
            wrapApiCall { movieService.getListOfGenresForMovies() }.results
                ?.let { remoteGenres ->
                    movieDao.insertGenresMovies(localGenresMovieMapper.map(remoteGenres))
                }

        } catch (_: Throwable) {
        }
    }


    override suspend fun getGenresTvs(): List<GenreEntity> {
        return domainGenreTvMapper.map(movieDao.getGenresTvs())
    }

    override suspend fun refreshGenresTv() {
        try {
            wrapApiCall { movieService.getListOfGenresForTvs() }.results
                ?.let { remoteGenres ->
                    movieDao.insertGenresTvs(localGenresTvMapper.map(remoteGenres))
                }

        } catch (_: Throwable) {
        }
    }
    /// endregion


    /// region refresh time
    override suspend fun getLastRefreshTime(): Long? {
        return preferenceStorage.lastRefreshTime
    }

    override suspend fun setLastRefreshTime(time: Long) {
        preferenceStorage.setLastRefreshTime(time)
    }

    override suspend fun refreshAll() {
        refreshGenres()
        refreshPopularMovies()
        refreshPopularPeople()
        refreshNowPlayingMovies()
        refreshTopRatedMovies()
        refreshTrendingMovies()
        refreshUpcomingMovies()
        refreshFavoriteMovies()
        refreshLists()
    }

    /// endregion

    /// region tv

    override suspend fun getAiringTodayTVShows(): Pager<Int, TVShowsEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { airingTodayTvShowsPagingSource }
        )
    }

    override suspend fun getTopRatedTVShows(): Pager<Int, TVShowsEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { topRatedTvShowsPagingSource }
        )
    }

    override suspend fun getPopularTVShows(): Pager<Int, TVShowsEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { popularTVShowsPagingSource }
        )
    }

    override suspend fun getOnTheAirTVShows(): Pager<Int, TVShowsEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { onTheAirTVShowsPagingSource }
        )
    }
    /// endregion

    override suspend fun getMoviesDetails(movieId: Int): MovieDetailsEntity {
        return domainMovieDetailsMapper.map(wrapApiCall { movieService.getMovieDetails(movieId) })
    }

    override suspend fun setMovieRate(movieId: Int, rate: Float): RatingEntity {
        return domainRatingMapper.map(
            wrapApiCall { movieService.setMovieRate(RatingRequest(rate), movieId) }
        )
    }


    //region my list
    override suspend fun getFavoriteMovies(): List<MovieEntity> {
        return domainFavoriteMoviesMapper.map(movieDao.getFavoriteMovies())
    }

    override suspend fun refreshFavoriteMovies() {
        refreshWrapper(
            movieService::getFavoriteMovies,
            localFavoriteMoviesMapper::map,
            movieDao::insertFavoriteMovie
        )
    }

    override suspend fun addFavoriteMovie(favoriteBody: FavoriteBodyRequestEntity): Boolean {
        return movieService.addFavoriteMovie(
            favoriteMoviesMapper.map(favoriteBody)
        ).isSuccessful
    }

    override suspend fun getFavoriteByMediaType(mediaType: String): List<MovieEntity> {
        refreshFavoriteByMediaType(mediaType)
        return domainFavoriteMoviesMapper.map(movieDao.getFavoriteByMediaType(mediaType))
    }

    private suspend fun refreshFavoriteByMediaType(mediaType: String) {
        refreshWrapper(
            apiCall = { movieService.getFavoriteByMediaType(mediaType = mediaType) },
            localFavoriteMoviesMapper::map,
            movieDao::insertFavoriteMovie
        )
    }


    override suspend fun getWatchlistByMediaType(mediaType: String): List<MovieEntity> {
        return domainWatchlistMapper.map(movieDao.getWatchlistByMediaType(mediaType))
    }


    override suspend fun addWatchlist(watchlistRequest: WatchlistRequestEntity): Boolean {

        val result = movieService.addWatchlist(
            watchlistMapper.map(watchlistRequest)
        ).isSuccessful

        if (result) {
            refreshWatchlistByMediaType(watchlistRequest.mediaType)
        }

        return result
    }

    private suspend fun refreshWatchlistByMediaType(mediaType: String) {
        refreshWrapper(
            apiCall = { movieService.getWatchlistByMediaType(mediaType = mediaType) },
            localWatchlistMapper::map,
            movieDao::insertWatchlist
        )
    }


    override suspend fun addList(name: String): Boolean {
        return movieService.addList(name).isSuccessful
    }

    override suspend fun getLists(): List<ListEntity> {
        return domainListMapper.map(movieDao.getLists())
    }

    override suspend fun refreshLists() {
        refreshWrapper(
            movieService::getLists,
            localListMapper::map,
            movieDao::insertList
        )
    }

//    override suspend fun addMovieToList(movie: ListMovieEntity ): Boolean {
//        val result = movieService.addMovieToList(movie.mediaId,).isSuccessful
//
//        if (result){
//            refreshAddMovieToList(movie)
//        }
//        return result
//    }

//    private suspend fun refreshAddMovieToList(movie: ListMovieEntity) {
//        movieDao.insertMovieToList(localListMovieMapper.map(movie))
//    }

    override suspend fun addMovieToList(movie: ListMovieEntity): Boolean {
        return movieService.addMovieToList(movie.mediaId).isSuccessful
    }

    override suspend fun getMovieList(): List<ListMovieEntity> {
        return domainListMovieMapper.map(movieDao.getMoviesList())
    }


    override suspend fun getDetailsList(listId: Int): List<MovieEntity> {
        val result =  wrapApiCall { movieService.getDetailsList(listId)}.items
        return    result?.map { item->
            domainMovieItemListMapper.map (item)
        }?: emptyList()
    }


    override suspend fun getListCreated(): List<ListCreatedEntity> {
        return wrapApiCall { movieService.getLists() }.results.also {
        }
            ?.filterNotNull()?.let { lists ->
                lists.map { input ->
                    ListCreatedEntity(
                        id = input.id,
                        itemCount = input.itemCount,
                        listType = input.listType,
                        name = input.name,
                        posterPath = getDetailsList(input.id?:0).also {
                        }.map { items ->
                             items.imageUrl
                        }
                    )
                }
            } ?: emptyList()
    }


    //endregion
}
