package com.chocolatecake.movieapp.data.repository

import com.chocolatecake.movieapp.data.local.database.MovieDao
import com.chocolatecake.movieapp.data.local.database.dto.SearchHistoryLocalDto
import com.chocolatecake.movieapp.data.remote.service.MovieService
import com.chocolatecake.movieapp.data.repository.mappers.cash.LocalGenresMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.cash.LocalPopularPeopleMapper
import com.chocolatecake.movieapp.data.repository.mappers.cash.movie.LocalNowPlayingMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.cash.movie.LocalPopularMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.cash.movie.LocalTopRatedMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.cash.movie.LocalTrendingMoviesMapper
import com.chocolatecake.movieapp.data.repository.mappers.cash.movie.LocalUpcomingMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.DomainGenreMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.DomainPeopleMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.movie.DomainMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.movie.DomainNowPlayingMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.movie.DomainPopularMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.movie.DomainTopRatedMovieMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.movie.DomainTrendingMoviesMapper
import com.chocolatecake.movieapp.data.repository.mappers.domain.movie.DomainUpcomingMovieMapper
import com.chocolatecake.movieapp.domain.entities.GenreEntity
import com.chocolatecake.movieapp.domain.entities.MovieEntity
import com.chocolatecake.movieapp.domain.entities.PeopleEntity
import com.chocolatecake.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao,
    private val localGenresMovieMapper: LocalGenresMovieMapper,
    private val localPopularMovieMapper: LocalPopularMovieMapper,
    private val localPopularPeopleMapper: LocalPopularPeopleMapper,
    private val localNowPlayingMovieMapper: LocalNowPlayingMovieMapper,
    private val localTopRatedMovieMapper: LocalTopRatedMovieMapper,
    private val localTrendingMoviesMapper: LocalTrendingMoviesMapper,
    private val localUpcomingMovieMapper: LocalUpcomingMovieMapper,
    private val domainMovieMapper: DomainMovieMapper,
    private val domainPopularMovieMapper: DomainPopularMovieMapper,
    private val domainNowPlayingMovieMapper: DomainNowPlayingMovieMapper,
    private val domainTopRatedMovieMapper: DomainTopRatedMovieMapper,
    private val domainUpcomingMovieMapper: DomainUpcomingMovieMapper,
    private val domainTrendingMovieMapper: DomainTrendingMoviesMapper,
    private val domainPeopleMapper: DomainPeopleMapper,
    private val domainGenreMapper: DomainGenreMapper,
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
    override suspend fun getSearchMovies(keyword: String): List<MovieEntity> {
        return wrapApiCall { movieService.getSearchMovies(keyword) }.results
            ?.filterNotNull()?.let { movieDtos -> domainMovieMapper.map(movieDtos) } ?: emptyList()
    }
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
    /// endregion
}
