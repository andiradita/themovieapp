package com.andiradita.themovieapp.data.remote

import com.andiradita.themovieapp.utils.Constants
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class LoadingState<out T> {
    object Loading : LoadingState<Nothing>()
    data class Success<out T>(val data: T) : LoadingState<T>()
    data class Error(val errorMessage: String) : LoadingState<Nothing>() {
        companion object {
            fun message(exception: Exception): String {
                return when (exception) {
                    is UnknownHostException -> Constants.ERROR_MESSAGE_UNKNOWN_HOST_EX
                    is SocketTimeoutException -> Constants.ERROR_MESSAGE_SOCKET_TIMEOUT_EX
                    is HttpException -> Constants.ERROR_MESSAGE_HTTP_EX
                    else -> Constants.ERROR_MESSAGE_DEFAULT
                }
            }
        }
    }
}