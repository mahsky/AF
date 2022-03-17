package com.example.af.main.viewmodel

import com.af.framework.net.Network
import com.af.model.Price
import com.example.af.db.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

/**
 * Created by mah on 2022/3/16.
 */
object HtmlParse {

    suspend fun parseHtml(house: House, html: String) = withContext(Dispatchers.IO) {
        val document = Jsoup.parse(html)
        val title = document.selectXpath("/html/body/div[3]/div/div/div[1]/h1")
        val price = document.selectXpath("/html/body/div[5]/div[2]/div[3]/div/span[1]")
        val img = document.selectXpath("/html/body/div[5]/div[1]/div[2]/ul/li[1]/img")
        val guaPaiTime = document.selectXpath("/html/body/div[7]/div[1]/div[1]/div/div/div[2]/div[2]/ul/li[1]/span[2]")
        val size = document.selectXpath("/html/body/div[5]/div[2]/div[4]/div[1]/div[1]")
        val xiaoqu = document.selectXpath("/html/body/div[5]/div[2]/div[5]/div[1]/a[1]")
        val xiajia = document.selectXpath("/html/body/div[3]/div/div/div[1]/h1/span")

        val priceValue = price.text().trim()

        val priceStatus = when {
            house.price.isNullOrEmpty() -> {
                HOUSE_PRICE_STAUS_NONE
            }
            house.price.toInt() == priceValue.toInt() -> {
                val lastPrice = getLastPrice(house)
                if (lastPrice != null && (System.currentTimeMillis() - lastPrice.time) < 1000 * 60 * 60 * 24 * 14) {
                    house.priceStatus
                } else {
                    HOUSE_PRICE_STAUS_NONE
                }
            }
            house.price.toInt() < priceValue.toInt() -> {
                HOUSE_PRICE_STAUS_UP
            }
            house.price.toInt() > priceValue.toInt() -> {
                HOUSE_PRICE_STAUS_DOWN
            }
            else -> HOUSE_PRICE_STAUS_NONE

        }
        val saveHouse = house.copy(
            id = house.id,
            title = title.text(),
            price = priceValue,
            priceJson = genPriceJson(house, priceValue),
            img = img.attr("src"),
            guaPaiTime = guaPaiTime.text(),
            size = size.text(),
            priceStatus = priceStatus,
            other1 = xiaoqu.text(),
            other3 = if ("已下架" == xiajia.text()) HOUSE_LIST_XIAJIA else HOUSE_LIST_ONLINE,
//            other4 = "0"
            other4 = ((house.other4?.toIntOrNull() ?: 0) + 1).toString()
        )
        println("------------house: $saveHouse")
        val houseDao = DB.db.houseDao()
        houseDao.update(saveHouse)
    }

    private fun getLastPrice(house: House): Price? {
        val moshi = Network.moshi
        val priceList = arrayListOf<Price>()
        val adapter: JsonAdapter<List<Price>> = moshi.adapter(Types.newParameterizedType(List::class.java, Price::class.java))
        return if (house.priceJson.isNullOrEmpty()) {
            null
        } else {
            adapter.fromJson(house.priceJson)?.apply {
                priceList.addAll(this)
            }
            priceList.maxByOrNull { it.id }
        }
    }

    private fun genPriceJson(house: House, price: String): String {
        val moshi = Network.moshi
        val priceList = arrayListOf<Price>()
        val adapter: JsonAdapter<List<Price>> = moshi.adapter(Types.newParameterizedType(List::class.java, Price::class.java))
        if (house.priceJson.isNullOrEmpty()) {
            priceList.add(Price(1, price = price, time = System.currentTimeMillis()))
        } else {
            adapter.fromJson(house.priceJson)?.apply {
                priceList.addAll(this)
            }
            val maxPrice = priceList.maxByOrNull { it.id }!!

            if (maxPrice.price.toInt() != price.toInt()) {
                priceList.add(Price(maxPrice.id + 1, price = price, time = System.currentTimeMillis()))
            }
        }

        return adapter.toJson(priceList)
    }
}