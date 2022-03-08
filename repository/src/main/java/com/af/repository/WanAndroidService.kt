package com.af.repository

import com.af.framework.net.ID
import com.af.model.WanAndroid
import com.af.model.WanAndroidBanner
import com.af.repository.common.Response
import retrofit2.http.GET

/**
 * Created by mah on 2022/3/4.
 */
interface WanAndroidService {
    @GET("/wenda/list/1/json")
    @ID("1001")
    suspend fun wenda(): Response<WanAndroid>

    @GET("/banner/json")
    @ID("1002")
    suspend fun banner(): Response<List<WanAndroidBanner>>
}