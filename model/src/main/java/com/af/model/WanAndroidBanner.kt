package com.af.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/3/7.
 */
@JsonClass(generateAdapter = true)
data class WanAndroidBanner(
    @Json(name = "data")
    val data: List<Data>,
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        val desc: String,
        val id: Int,
        val imagePath: String,
        val isVisible: Int,
        val order: Int,
        val title: String,
        val type: Int,
        val url: String
    )
}

