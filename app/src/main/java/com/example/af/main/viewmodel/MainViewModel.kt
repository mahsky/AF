package com.example.af.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.af.framework.net.Network
import com.af.framework.net.NetworkResponse
import com.af.model.Price
import com.af.repository.LianJiaRepository
import com.af.repository.common.onFailure
import com.af.repository.common.onSuccess
import com.example.af.db.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.lang.StringBuilder

/**
 * 单向数据流 (UDF)
 * https://developer.android.com/jetpack/guide/ui-layer?hl=zh-cn
 */
data class MainUiState(
    val status: String
)

/**
 * Created by mah on 2022/2/23.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState(""))
    val uiState = _uiState.asStateFlow()

    val houses: LiveData<List<House>> = DB.db.houseDao().getAll()

    fun load(house: House) = viewModelScope.launch {
        val response = LianJiaRepository()
            .dongYiQu(house.id)
            .onFailure { error ->
                _uiState.update { it.copy(status = "${error.status} ${error.id}") }
            }.onSuccess { text ->
                _uiState.update { it.copy(status = "http request success") }
            }

        val html = when (response) {
            is NetworkResponse.Success -> response.data
            else -> {
                return@launch
            }
        }
        parseHtml(house, html)
    }

    private suspend fun parseHtml(house: House, html: String) = withContext(Dispatchers.IO) {
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
                if (lastPrice != null && (System.currentTimeMillis() - lastPrice.time) < 1000 * 60 * 60 * 24 * 7) {
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
            other3 = if ("已下架" == xiajia.text()) HOUSE_LIST_XIAJIA else HOUSE_LIST_ONLINE
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

    fun insertHouse(id: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (houses.value?.find { it.id == id } != null) {
                    _uiState.update { it.copy(status = "已有此房源") }
                    delay(1000)
                    _uiState.update { it.copy(status = "") }
                    return@withContext
                }
                val house = House(id)
                val houseDao = DB.db.houseDao()
                houseDao.insertAll(house)
                load(house).join()
                delay(1000)
                _uiState.update { it.copy(status = "") }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                houses.value?.forEach { house ->
                    _uiState.update { it.copy(status = "update id: ${house.id}") }
                    load(house).join()
                    _uiState.update { it.copy(status = "update finish id: ${house.id}") }
                    delay(1000)
                    _uiState.update { it.copy(status = "") }
                }
            }
        }
    }

    fun delete(house: House) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                DB.db.houseDao().delete(house)
            }
        }
    }

    fun remark(house: House, remark: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val h = house.copy(other2 = remark.trim())
                DB.db.houseDao().update(h)
            }
        }
    }
}