package com.example.af.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.af.repository.WanAndroidRepository
import com.af.repository.common.onFailure
import com.af.repository.common.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 单向数据流 (UDF)
 * https://developer.android.com/jetpack/guide/ui-layer?hl=zh-cn
 */
data class MainUiState(
    val name: String,
    val gender: String
)

/**
 * Created by mah on 2022/2/23.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState("Hello World!", "Women"))
    val uiState = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            WanAndroidRepository()
                .banner()
                .onFailure { error ->
                    _uiState.update { it.copy(name = "${error.status} ${error.id}") }
                }.onSuccess { banners ->
                    _uiState.update { it.copy(name = banners.data.firstOrNull()?.title ?: "empty") }
                }

            WanAndroidRepository()
                .cropBanner()
                .onFailure { error ->
                    _uiState.update { it.copy(name = "${error.status} ${error.id}") }
                }.onSuccess { banners ->
                    _uiState.update { it.copy(name = banners.firstOrNull()?.title ?: "empty") }
                }
        }
    }
}