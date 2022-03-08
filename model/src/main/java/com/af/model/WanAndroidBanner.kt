package com.af.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/3/7.
 */
@JsonClass(generateAdapter = true)
data class WanAndroidBanner(
    @Json(name = "title")
    val title: String
)
