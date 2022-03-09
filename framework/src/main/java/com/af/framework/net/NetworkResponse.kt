package com.af.framework.net

import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any>(open val id: String = "") {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val data: T, override val id: String = "") : NetworkResponse<T, Nothing>()

    /**
     * Success response with body
     * http code 200
     */
    data class BizError(val code: Int, val msg: String, override val id: String = "") : NetworkResponse<Nothing, Nothing>()

    /**
     * Failure response with body
     * http code 非200
     */
    data class ApiError<U : Any>(val body: U, val httpCode: Int, override val id: String = "") : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException, override val id: String = "") : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?, override val id: String = "") : NetworkResponse<Nothing, Nothing>()
}