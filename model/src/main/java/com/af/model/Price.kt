package com.af.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/3/12.
 */
@JsonClass(generateAdapter = true)
data class Price(
    @Json(name = "id")
    val id: Int,
    @Json(name = "time")
    val time: Long,
    @Json(name = "price")
    val price: String,
)