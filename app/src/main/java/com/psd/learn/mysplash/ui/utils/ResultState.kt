package com.psd.learn.mysplash.ui.utils

import java.lang.Exception

sealed class ResultState {
    data object Loading : ResultState()
    data class Success<T>(val data: T) : ResultState()
    data class Error(val exception: Exception) : ResultState()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading..."
        }
    }
}

/*
sealed class LoggedState {
    data class OnSuccess(val data: List<XYZ>) : LoggedState()
    object OnEmpty : LoggedState()
    data class IsLoading(val isLoading: Boolean = true) : LoggedState()
    data class OnError(val message: String) : LoggedState()

    https://velog.io/@wonseok/StateFlow-SharedFlow
    https://stackoverflow.com/questions/67413165/kotlin-flow-how-can-i-get-the-cache-data-from-subscription-in-flow-when-i-have
} */
