package com.af.framework.net

import okhttp3.*


/**
 * Created by mah on 2022/3/2.
 */
class NetworkParameterInterceptor(private val networkParameterAdapter: NetworkParameterAdapter) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val urlBuilder = request.url.newBuilder()
        when (request.method) {
            "GET" -> {
                val parameter = networkParameterAdapter.getGetParameter(request)
                parameter.forEach {
                    urlBuilder.addQueryParameter(it.key, it.value)
                }
                request.newBuilder().url(urlBuilder.build()).build()
            }
            "POST" -> {
                val queryParameter = networkParameterAdapter.getPostQueryParameter(request)
                queryParameter.forEach {
                    urlBuilder.addQueryParameter(it.key, it.value)
                }
                request = request.newBuilder().url(urlBuilder.build()).build()
                if (request.body is FormBody) {
                    val builder = FormBody.Builder()
                    val body = request.body as FormBody
                    for (i in 0 until body.size) {
                        builder.add(body.encodedName(i), body.encodedValue(i))
                    }
                    val fieldParameter = networkParameterAdapter.getPostFieldParameter(request)
                    fieldParameter.forEach {
                        builder.add(it.key, it.value)
                    }
                    request = request.newBuilder().post(builder.build()).build()
                }
            }
        }
        return chain.proceed(request)
    }
}