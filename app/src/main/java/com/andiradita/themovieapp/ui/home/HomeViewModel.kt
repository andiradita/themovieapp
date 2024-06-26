package com.andiradita.themovieapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.MovieResponse
import com.andiradita.themovieapp.utils.Constants
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()
    private var movieResponse: MovieResponse? = null
    private var _discoverMovieResponse = MutableLiveData<LoadingState<MovieResponse?>>()
    var discoverMovieResponse: LiveData<LoadingState<MovieResponse?>> = _discoverMovieResponse
    var page = 1


    fun getDiscoverMovie(genreIds: String) {
        viewModelScope.launch {
            try {
                _discoverMovieResponse.postValue(LoadingState.Loading)
                val response = repository.getDiscoverMovie(Constants.LANGUAGE_EN, page, genreIds)
                _discoverMovieResponse.postValue(discoverResponse(response))
            } catch (e: Exception) {
                _discoverMovieResponse.postValue(LoadingState.Error(LoadingState.Error.message(e)))
            }

        }
    }

    private fun discoverResponse(response: Response<MovieResponse>): LoadingState<MovieResponse?> {
        return if (response.isSuccessful) {
            response.body()?.let {
                if (page == 1) movieResponse = null
                page++;
                if (movieResponse == null) {
                    movieResponse = it
                } else {
                    val oldValue = movieResponse?.results
                    val newValue = it.results
                    oldValue?.addAll(newValue)
                }
            }
            LoadingState.Success(movieResponse ?: response.body())
        } else LoadingState.Error(
            try {
                response.errorBody()?.string()?.let {
                    JSONObject(it).get("status_message")
                }
            } catch (e: Exception) {
                LoadingState.Error.message(e)
            } as String
        )
    }
}