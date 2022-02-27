package com.example.nasa.utils

interface CallbackData<T> {

    fun onSuccess(result: T)

    fun onError(err: Throwable)

}