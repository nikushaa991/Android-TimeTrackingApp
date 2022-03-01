package com.example.timetrackingapp.shared

sealed class Response<T>(val data: T? = null, val throwable: Throwable? = null) {
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(data: T? = null, throwable: Throwable?) : Response<T>(data, throwable)
    class Loading<T>() : Response<T>()
}