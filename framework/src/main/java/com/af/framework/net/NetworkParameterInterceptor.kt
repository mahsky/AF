package com.af.framework.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by mah on 2022/3/2.
 */
class NetworkParameterInterceptor(private val networkParameterConfig: NetworkParameterAdapter) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val parameter = networkParameterConfig.getGetParameter(request)

        val urlBuilder = request.url.newBuilder()
        parameter.forEach {
            urlBuilder.addQueryParameter(it.key, it.value)
        }
        val newRequest = request.newBuilder()
            .method(request.method, request.body)
            .url(urlBuilder.build()).build()
        return chain.proceed(newRequest)
    }
}