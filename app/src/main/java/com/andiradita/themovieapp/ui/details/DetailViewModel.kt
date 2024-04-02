package com.andiradita.themovieapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.DetailMovieResponse
import com.andiradita.themovieapp.model.ReviewResponse
import retrofit2.HttpException

class DetailViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepository()
    private var page = 1
    fun getDetailMovie(id: Int): LiveData<LoadingState<DetailMovieResponse>> = liveData {
        emit(LoadingState.Loading)
        try {
            val response = repository.getDetailMovie("en", id.toString())
            emit(LoadingState.Success(response))
        } catch (e: HttpException) {
            e.response()?.errorBody()?.string()?.let { LoadingState.Error(it) }?.let { emit(it) }
        }
    }

    fun getReviewMovie(id: Int): LiveData<LoadingState<ReviewResponse?>?> = liveData {
        emit(LoadingState.Loading)
        try {
            val response = repository.getReviewMovie("en", page, id.toString())
            emit(LoadingState.Success(response))
        } catch (e: HttpException) {
            e.response()?.errorBody()?.string()?.let { LoadingState.Error(it) }?.let { emit(it) }
        }
    }
}