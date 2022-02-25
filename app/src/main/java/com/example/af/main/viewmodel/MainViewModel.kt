package com.example.af.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.af.framework.net.onFailure
import com.af.framework.net.onSuccess
import com.af.repository.ApiRepository
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
            ApiRepository()
                .getSuccess()
                .onFailure {
                    _uiState.update { it.copy(name = "error") }
                }
                .onSuccess { data ->
                    _uiState.update { it.copy(name = data.name) }
                }
        }
    }
}