package com.af.repository

import com.af.framework.net.ID
import com.af.model.User
import com.af.repository.common.Response
import retrofit2.http.GET

/**
 * Created by mah on 2022/2/25.
 */
interface ApiService {
    @GET("success")
    @ID("1001")
    suspend fun getSuccess(): Response<User>

    @GET("error")
    suspend fun getError(): Response<User>
}