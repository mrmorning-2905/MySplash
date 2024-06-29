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
