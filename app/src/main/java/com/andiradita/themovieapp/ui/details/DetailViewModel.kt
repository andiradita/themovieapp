package com.andiradita.themovieapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.DetailMovieResponse
import com.andiradita.themovieapp.model.ReviewResponse
import com.andiradita.themovieapp.utils.Constants

class DetailViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepository()
    private var page = 1
    fun getDetailMovie(id: Int): LiveData<LoadingState<DetailMovieResponse>> = liveData {
        emit(LoadingState.Loading)
        try {
            val response = repository.getDetailMovie(Constants.LANGUAGE_EN, id.toString())
            emit(LoadingState.Success(response))
        } catch (e: Exception) {
            emit(LoadingState.Error(LoadingState.Error.message(e)))
        }
    }

    fun getReviewMovie(id: Int): LiveData<LoadingState<ReviewResponse?>?> = liveData {
        emit(LoadingState.Loading)
        try {
            val response = repository.getReviewMovie(Constants.LANGUAGE_EN, page, id.toString())
            emit(LoadingState.Success(response.body()))
        } catch (e: Exception) {
            emit(LoadingState.Error(LoadingState.Error.message(e)))
        }
    }
}