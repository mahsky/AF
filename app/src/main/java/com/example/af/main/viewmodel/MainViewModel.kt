package com.example.af.main.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.af.framework.net.Network
import com.af.model.AppItem
import com.example.af.APPS
import com.example.af.dataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Created by mah on 2022/2/23.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _apps = MutableLiveData<List<AppItem>>()
    val apps: LiveData<List<AppItem>> = _apps

    private val _findApps = MutableLiveData<List<AppItem>>()
    val findApps: LiveData<List<AppItem>> = _findApps

    fun load(packageManager: PackageManager) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.data.collect {
                val apps = it[APPS]
                if (apps != null && apps.isNotEmpty()) {
                    _apps.value = AppNameFindUseCase.jsonToApps(apps)
                }
            }
        }
        viewModelScope.launch {
            val apps = AppNameFindUseCase.getApps(packageManager)
            val json = AppNameFindUseCase.appsToJson(apps)
            if (json.isNotEmpty()) {
                getApplication<Application>().dataStore.edit {
                    it[APPS] = json
                }
            }
            _apps.value = apps
            apps.forEach {
                println("app name: ${it.appName} ${it.letterAppName}")
            }
        }
    }

    fun findApp(name: String) {
        viewModelScope.launch {
            _findApps.value = AppNameFindUseCase.findApp(name, _apps.value ?: mutableListOf())
        }
    }
}

