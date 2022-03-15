package com.example.af.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by mah on 2022/3/11.
 */
@Entity
data class House(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String? = "",
    @ColumnInfo(name = "price") val price: String? = "",
    @ColumnInfo(name = "priceJson") val priceJson: String? = "",
    @ColumnInfo(name = "img") val img: String? = "",
    @ColumnInfo(name = "guaPaiTime") val guaPaiTime: String? = "",
    @ColumnInfo(name = "size") val size: String? = "",
    @ColumnInfo(name = "priceStatus") val priceStatus: String? = "",
    @ColumnInfo(name = "text") val text: String? = "",
    @ColumnInfo(name = "other1") val other1: String? = "",//小区
    @ColumnInfo(name = "other2") val other2: String? = "",//备注
    @ColumnInfo(name = "other3") val other3: String? = "",//上架状态
    @ColumnInfo(name = "other4") val other4: String? = "",
    @ColumnInfo(name = "other5") val other5: String? = "",
    @ColumnInfo(name = "status") val status: String = ""
)

val HOUSE_STATUS_NONE = 0
val HOUSE_STATUS_UPDING = 1
val HOUSE_STATUS_UPDING_SUCCESS = 2
val HOUSE_STATUS_UPDING_ERROR = 3

val HOUSE_PRICE_STAUS_NONE = "0"
val HOUSE_PRICE_STAUS_UP = "1"
val HOUSE_PRICE_STAUS_DOWN = "2"

val HOUSE_LIST_ONLINE = "0"
val HOUSE_LIST_XIAJIA = "1"