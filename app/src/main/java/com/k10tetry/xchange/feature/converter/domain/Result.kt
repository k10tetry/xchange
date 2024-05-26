package com.k10tetry.xchange.feature.converter.domain

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Loading<T>() : Resource<T>()
    class Error<T>(data: T? = null, message: String?) : Resource<T>(message = message)
}