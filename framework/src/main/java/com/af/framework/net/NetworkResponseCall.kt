package com.af.framework.net

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

internal class NetworkResponseCall<S : Any, E : Any>(
    private val delegate: Call<NetworkResponse<S, E>>,
    private val errorConverter: Converter<ResponseBody, E>,
    private val responseListener: ((response: NetworkResponse<Any, Any>) -> Unit)? = null,
    private val id: String = ""
) : Call<NetworkResponse<S, E>> {

    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) {
        return delegate.enqueue(object : Callback<NetworkResponse<S, E>> {
            override fun onResponse(
                call: Call<NetworkResponse<S, E>>,
                response: Response<NetworkResponse<S, E>>
            ) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        when (body) {
                            is NetworkResponse.Success -> body.copy(id = id)
                            is NetworkResponse.BizError -> body.copy(id = id)
                            else -> {
                                NetworkResponse.UnknownError(null, id)
                            }
                        }
                    } else {
                        NetworkResponse.UnknownError(null, id)
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            null
                        }
                    }
                    if (errorBody != null) {
                        NetworkResponse.ApiError(errorBody, code, id)
                    } else {
                        NetworkResponse.UnknownError(null, id)
                    }
                }.let {
                    callback.onResponse(
                        this@NetworkResponseCall,
                        Response.success(it.apply {
                            responseListener?.invoke(this)
                        })
                    )
                }
            }

            override fun onFailure(call: Call<NetworkResponse<S, E>>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> NetworkResponse.NetworkError(throwable, id)
                    else -> NetworkResponse.UnknownError(throwable, id)
                }.apply {
                    responseListener?.invoke(this)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter, responseListener)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
