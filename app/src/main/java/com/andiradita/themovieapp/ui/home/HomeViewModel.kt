package com.andiradita.themovieapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.MovieResponse
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val repository: MovieRepository = MovieRepository()
    private var page = 1
    private val language = "en"
    private var movieResponse: MovieResponse? = null
    private var _discoverMovieResponse = MutableLiveData<LoadingState<MovieResponse?>>()
    var discoverMovieResponse: LiveData<LoadingState<MovieResponse?>> = _discoverMovieResponse

    fun getDiscoverMovie() {
        viewModelScope.launch {
            _discoverMovieResponse.postValue(LoadingState.Loading)
            val response = repository.getDiscoverMovie(language, page, "28")
            _discoverMovieResponse.postValue(discoverResponse(response))
        }
    }

    private fun discoverResponse(response: Response<MovieResponse>): LoadingState<MovieResponse?> {
        return if (response.isSuccessful) {
            response.body()?.let {
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
            } catch (e: JSONException) {
                e.localizedMessage
            } as String
        )
    }

//    fun getDiscoverMovie(): LiveData<LoadingState<MovieResponse>> = liveData {
//        emit(LoadingState.Loading)
//        try {
//            val response = repos.getDiscoverMovie(language, page, "28")
//            emit(LoadingState.Success(response))
//        } catch (e: HttpException) {
//            e.response()?.errorBody()?.string()?.let { LoadingState.Error(it) }?.let { emit(it) }
//        }
//    }
}