package com.af.repository.common

import com.af.framework.net.NetworkResponse
import com.af.model.Error

typealias Response<T> = NetworkResponse<T, Error>

const val NETWORK_NET_ERROR = "-1"
const val NETWORK_UNKNOWN_ERROR = "-2"

fun <T : Any> Response<T>.onFailure(failure: (u: Error) -> Unit): Response<T> {
    when (this) {
        is NetworkResponse.Success -> {}
        is NetworkResponse.ApiError -> {
            failure(Error(body.toString(), code.toString()))
        }
        is NetworkResponse.NetworkError -> {
            failure(Error(NETWORK_NET_ERROR, "network error"))
        }
        is NetworkResponse.UnknownError -> {
            failure(Error(NETWORK_UNKNOWN_ERROR, "unknown error"))
        }
    }
    return this
}

fun <T : Any> Response<T>.onSuccess(success: (t: T) -> Unit): Response<T> {
    if (this is NetworkResponse.Success) {
        success(body)
    }
    return this
}