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

    private val retrofitMap = mutableMapOf<String, Retrofit>()

    private val moshi by lazy {
        Moshi.Builder().build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    })
                }
            }
            .build()
    }

    fun getRetrofit(baseUrl: String): Retrofit = retrofitMap.getOrPut(baseUrl) {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }
}