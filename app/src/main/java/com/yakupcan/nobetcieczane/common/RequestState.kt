package com.yakupcan.nobetcieczane.common

sealed class RequestState<out T>(val data: T? = null) {
    class Loading : RequestState<Nothing>()
    class Error(val exception : Throwable) : RequestState<Nothing>()
    class Success<out T>(data: T) : RequestState<T>(data)
}
