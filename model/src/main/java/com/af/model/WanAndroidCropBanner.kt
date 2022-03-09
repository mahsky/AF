package com.af.model

import com.af.framework.net.CropEnvelope
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/3/7.
 */
@JsonClass(generateAdapter = true)
@CropEnvelope
data class WanAndroidCropBanner(
    @Json(name = "title")
    val title: String
)
