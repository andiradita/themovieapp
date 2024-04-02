package com.andiradita.themovieapp.data.repository

import com.andiradita.themovieapp.BuildConfig
import com.andiradita.themovieapp.data.remote.RetrofitClient

class MovieRepository() {

    private val client = RetrofitClient.getApiService()
    suspend fun getAllMovieGenre() = client.getGenreMovie(auth = BuildConfig.API_KEY)
    suspend fun getDiscoverMovie(lang: String, page: Int, genre: String) =
        client.getDiscoverMovie(auth = BuildConfig.API_KEY, lang, page, genre)

    suspend fun getDetailMovie(lang: String, id: String) =
        client.getDetailMovie(auth = BuildConfig.API_KEY, id, lang)

    suspend fun getReviewMovie(lang: String, page: Int, id: String) =
        client.getReviewMovie(auth = BuildConfig.API_KEY, id, lang, page)

    suspend fun getTrailerMovie(id: String) =
        client.getTrailerMovie(auth = BuildConfig.API_KEY, id)

    suspend fun getNowPlayingMovie(lang: String, page: Int) =
        client.getNowPlayingMovie(auth = BuildConfig.API_KEY, lang, page)

    suspend fun getTopRatedMovie(lang: String, page: Int) =
        client.getTopRatedMovie(auth = BuildConfig.API_KEY, lang, page)
}