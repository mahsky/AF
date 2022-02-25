package com.af.framework.net

import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}


fun <T : Any, U : Any> NetworkResponse<T, U>.onFailure(failure: (u: NetworkResponse<T, U>) -> Unit): NetworkResponse<T, U> {
    when (this) {
        is NetworkResponse.Success -> {}
        else -> failure(this)
    }
    return this
}

fun <T : Any, U : Any> NetworkResponse<T, U>.onSuccess(success: (t: T) -> Unit): NetworkResponse<T, U> {
    if (this is NetworkResponse.Success) {
        success(body)
    }
    return this
}