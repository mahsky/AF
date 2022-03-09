package com.af.model

import com.af.framework.net.CropEnvelope
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/3/9.
 */
@JsonClass(generateAdapter = true)
@CropEnvelope
data class WanAndroidUserInfo(
    @Json(name = "errorMsg")
    val errorMsg: String
)
