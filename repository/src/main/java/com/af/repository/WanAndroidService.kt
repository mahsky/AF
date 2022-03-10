package com.af.repository

import com.af.framework.net.ID
import com.af.model.WanAndroidCropBanner
import com.af.model.WanAndroidBanner
import com.af.model.WanAndroidUserInfo
import com.af.repository.common.ReqId
import com.af.repository.common.Response
import retrofit2.http.GET

/**
 * Created by mah on 2022/3/4.
 */
interface WanAndroidService {
    @GET("/banner/json")
    @ID(ReqId.ID_1)
    suspend fun banner(): Response<WanAndroidBanner>

    @GET("/banner/json")
    @ID(ReqId.ID_2)
    suspend fun cropBanner(): Response<List<WanAndroidCropBanner>>
}