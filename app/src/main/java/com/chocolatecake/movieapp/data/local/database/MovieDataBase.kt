package com.chocolatecake.movieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chocolatecake.movieapp.data.local.database.entity.movie.NowPlayingMovieEntity
import com.chocolatecake.movieapp.data.local.database.entity.movie.PopularMovieEntity
import com.chocolatecake.movieapp.data.local.database.entity.movie.TopRatedMovieEntity
import com.chocolatecake.movieapp.data.local.database.entity.movie.UpcomingMovieEntity

@Database(
    entities = [
        PopularMovieEntity::class,
        TopRatedMovieEntity::class,
        UpcomingMovieEntity::class,
        NowPlayingMovieEntity::class,
        ],
    version = 1,
    exportSchema = false
)
abstract class MovieDataBase: RoomDatabase() {
    abstract val movieDao: MovieDao
}