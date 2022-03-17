package com.af.framework.net

import com.af.framework.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by mah on 2022/2/25.
 */
object Network {

    /**
     * retrofit 多实例
     */
    private val retrofitMap = mutableMapOf<String, Retrofit>()

    /**
     * 统一监听response , 用于统一的错误处理
     * 非主线程回调
     */
    val responseListener = arrayListOf<((response: NetworkResponse<Any, Any>) -> Unit)>()

    /**
     * 公共参数配置
     */
    var networkParameterAdapter: NetworkParameterAdapter? = null

    val moshi by lazy {
        Moshi.Builder()
            .add(NetworkMoshiAdapterFactory())
            .build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BASIC)
                    })
                }
            }
            .addInterceptor(NetworkParameterInterceptor(NetworkParameterAdapterFactory().create(networkParameterAdapter)))
            .build()
    }

    private val networkResponseAdapterFactory by lazy {
        NetworkResponseAdapterFactory { response ->
            responseListener.forEach {
                it.invoke(response)
            }
        }
    }

    fun getRetrofit(baseUrl: String): Retrofit = retrofitMap.getOrPut(baseUrl) {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(networkResponseAdapterFactory)
            .addConverterFactory(NetworkBaseTypeConverterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }
}