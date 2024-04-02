package com.andiradita.themovieapp.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andiradita.themovieapp.data.remote.LoadingState
import com.andiradita.themovieapp.data.repository.MovieRepository
import com.andiradita.themovieapp.model.ReviewResponse
import com.andiradita.themovieapp.utils.Constants
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class ReviewViewModel : ViewModel() {
    private val repository: MovieRepository = MovieRepository()
    private var page = 1
    private var reviewResponse: ReviewResponse? = null
    private var _movieReviewResponse = MutableLiveData<LoadingState<ReviewResponse?>>()
    var movieReviewResponse: LiveData<LoadingState<ReviewResponse?>> = _movieReviewResponse

    fun getReviewMovie(id: Int) {
        viewModelScope.launch {
            try {
                _movieReviewResponse.postValue(LoadingState.Loading)
                val response = repository.getReviewMovie(Constants.LANGUAGE_EN, page, id.toString())
                _movieReviewResponse.postValue(handleReviewResponse(response))
            } catch (e: Exception) {
                _movieReviewResponse.postValue(LoadingState.Error(LoadingState.Error.message(e)))
            }
        }
    }

    private fun handleReviewResponse(response: Response<ReviewResponse>): LoadingState<ReviewResponse?> {
        return if (response.isSuccessful) {
            response.body()?.let {
                page++;
                if (reviewResponse == null) {
                    reviewResponse = it
                } else {
                    val oldValue = reviewResponse?.results
                    val newValue = it.results
                    oldValue?.addAll(newValue)
                }
            }
            LoadingState.Success(reviewResponse ?: response.body())
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