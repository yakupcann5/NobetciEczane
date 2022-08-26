package com.android.nobetcieczane.common

sealed class RequestState<T>(val data: T? = null) {
    class Loading<T> : RequestState<T>()
    class Error<T>(val exception: Throwable) : RequestState<T>()
    class Success<T>(data: T) : RequestState<T>(data)
}
