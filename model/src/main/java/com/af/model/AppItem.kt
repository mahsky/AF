package com.af.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/7/11.
 */

@JsonClass(generateAdapter = true)
data class AppItem(
    @Json(name = "package")
    val packageName: String,

    @Json(name = "letter_name")
    val letterAppName: String,

    @Json(name = "name")
    val appName: String,

    @Json(ignore = true)
    var sort: Int = 0
)
