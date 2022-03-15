package com.af.repository

import com.af.repository.common.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mah on 2022/3/11.
 */
interface LianJIaService {
    @GET("/ershoufang/{id}.html")
    suspend fun dongYiQu(@Path("id") id: String): Response<String>
}