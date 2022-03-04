package com.af.repository

import com.af.model.WanAndroid
import com.af.repository.common.Response
import retrofit2.http.GET

/**
 * Created by mah on 2022/3/4.
 */
interface WanAndroidService {
    @GET("/wenda/list/1/json")
    suspend fun wenda(): Response<WanAndroid>
}