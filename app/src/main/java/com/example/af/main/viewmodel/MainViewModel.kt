package com.example.af.main.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Created by mah on 2022/2/23.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _apps = MutableLiveData<List<App>>()
    val apps: LiveData<List<App>> = _apps

    private val _findApps = MutableLiveData<List<App>>()
    val findApps: LiveData<List<App>> = _findApps

    fun load(packageManager: PackageManager) {
        viewModelScope.launch {
            val apps = AppNameFindUseCase.getApps(packageManager)
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

