package com.af.model

import com.af.framework.net.CropEnvelope
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/3/4.
 */
@JsonClass(generateAdapter = true)
data class WanAndroid(
    @Json(name = "curPage")
    val curPage: Int
) : CropEnvelope
