package com.andiradita.themovieapp.ui.genre

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.GenresItem
import retrofit2.HttpException

class GenreViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepository()
    fun getGenreMovies(): LiveData<LoadingState<List<GenresItem>?>> = liveData {
        emit(LoadingState.Loading)
        try {
            val response = repository.getAllMovieGenre()
            emit(LoadingState.Success(response.genres))
        } catch (e: HttpException) {
            e.response()?.errorBody()?.string()?.let { LoadingState.Error(it) }?.let { emit(it) }
        }
    }
}