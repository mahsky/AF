package com.af.framework.net

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by mah on 2022/2/25.
 */
object Network {
    private val moshi by lazy {
        Moshi.Builder().build()
    }
    private val okHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://retroftcoroutines.free.beeceptor.com/")
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }
}