package com.andiradita.themovieapp.data.remote

import com.andiradita.themovieapp.model.DetailMovieResponse
import com.andiradita.themovieapp.model.MovieGenreResponse
import com.andiradita.themovieapp.model.MovieResponse
import com.andiradita.themovieapp.model.MovieTrailerResponse
import com.andiradita.themovieapp.model.ReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Header("Authorization") auth: String,
        @Path("movie_id") movieId: String,
        @Query("language") lang: String?,
    ): DetailMovieResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviewMovie(
        @Header("Authorization") auth: String,
        @Path("movie_id") movieId: String,
        @Query("language") lang: String?,
        @Query("page") page: Int,
    ): Response<ReviewResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getTrailerMovie(
        @Header("Authorization") auth: String,
        @Path("movie_id") movieId: String
    ): MovieTrailerResponse
}