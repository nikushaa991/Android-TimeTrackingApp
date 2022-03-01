package com.example.timetrackingapp.shared

import io.reactivex.rxjava3.core.Observable

fun <T : Any> Observable<T>.toResponseObservable(): Observable<Response<T>> =
    this.map<Response<T>> { Response.Success(it) }
        .onErrorReturn { Response.Error(throwable = it) }
        .startWithItem(Response.Loading())
