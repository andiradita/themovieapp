package com.andiradita.themovieapp.ui.nowplaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.NowPlayingResponse
import com.andiradita.themovieapp.utils.Constants

class NowPlayingViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepository()
    private var page = 1

    fun getNowPlayingMovie(): LiveData<LoadingState<NowPlayingResponse>> = liveData {
        emit(LoadingState.Loading)
        try {
            val response = repository.getNowPlayingMovie(Constants.LANGUAGE_EN, page)
            emit(LoadingState.Success(response))
        } catch (e: Exception) {
            emit(LoadingState.Error(LoadingState.Error.message(e)))
        }
    }
}