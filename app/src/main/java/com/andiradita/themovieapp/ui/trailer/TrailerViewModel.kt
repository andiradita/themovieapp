package com.andiradita.themovieapp.ui.trailer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.MovieTrailerResponse
import retrofit2.HttpException

class TrailerViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepository()
    fun getTrailerMovie(id: Int): LiveData<LoadingState<MovieTrailerResponse>?> = liveData {
        emit(LoadingState.Loading)
        try {
            val response = repository.getTrailerMovie(id.toString())
            emit(LoadingState.Success(response))
        } catch (e: HttpException) {
            e.response()?.errorBody()?.string()?.let { LoadingState.Error(it) }?.let { emit(it) }
        }
    }
}