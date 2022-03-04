package com.af.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Error(
    @Json(name = "status")
    val status: String,

    @Json(name = "message")
    val message: String,

    val id: String = ""
)