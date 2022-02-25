package com.af.repository

import com.af.framework.net.NetworkResponse
import com.af.model.Success
import com.af.model.Error
import retrofit2.http.GET

/**
 * Created by mah on 2022/2/25.
 */
interface ApiService {
    @GET("success")
    suspend fun getSuccess(): NetworkResponse<Success, Error>

    @GET("error")
    suspend fun getError(): NetworkResponse<Success, Error>
}