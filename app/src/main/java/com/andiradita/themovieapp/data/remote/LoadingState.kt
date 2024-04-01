package com.andiradita.themovieapp.data.remote

sealed class LoadingState<out R> private constructor() {
    object Loading : LoadingState<Nothing>()
    data class Success<out T>(val data: T) : LoadingState<T>()
    data class Error(val errorMessage: String) : LoadingState<Nothing>()
}