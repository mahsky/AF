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
        HtmlParse.parseHtml(house, html)
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
                    delay((1000..3000).random().toLong())
                    _uiState.update { it.copy(status = "") }
                }
            }
        }
    }


    fun clear() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                houses.value?.forEach { house ->
                    DB.db.houseDao().update(house.copy(other4 = "0"))
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