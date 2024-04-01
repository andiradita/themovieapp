package com.andiradita.themovieapp.data.remote

import com.andiradita.themovieapp.model.MovieGenreResponse
import com.andiradita.themovieapp.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieService {

    @GET("discover/movie")
    suspend fun getDiscoverMovie(
        @Header("Authorization") auth: String,
        @Query("language") lang: String?,
        @Query("page") page: Int,
        @Query("with_genres") genre: String?,
    ): Response<MovieResponse>


    @GET("genre/movie/list")
    suspend fun getGenreMovie(
        @Header("Authorization") auth: String
    ): MovieGenreResponse
}