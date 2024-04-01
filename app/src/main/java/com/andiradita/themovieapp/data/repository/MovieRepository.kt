package com.andiradita.themovieapp.data.repository

import com.andiradita.themovieapp.BuildConfig
import com.andiradita.themovieapp.data.remote.RetrofitClient

class MovieRepository() {

    private val client = RetrofitClient.getApiService()
    suspend fun getAllMovieGenre() = client.getGenreMovie(auth = BuildConfig.API_KEY)
    suspend fun getDiscoverMovie(lang: String, page: Int, genre: String) =
        client.getDiscoverMovie(auth = BuildConfig.API_KEY, lang, page, genre)
}