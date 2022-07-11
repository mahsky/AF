package com.example.af

import android.content.pm.PackageInfo
import com.example.af.main.viewmodel.AppItem
import com.example.af.main.viewmodel.AppNameFindUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Created by mah on 2022/7/11.
 */
class AppNameUnitTest {
    @Test
    fun check_app_name() {
        val names = Data.names.split(",")
        val appItems = mutableListOf<AppItem>()
        names.forEach {
            appItems.add(AppItem(PackageInfo(), it, it))
        }

        runBlocking {
            val findApps = AppNameFindUseCase.findApp("dd", appItems)
            findApps.forEach {
                println("====: ${it.appName}")
            }
        }
    }

    @Test
    fun check_app_name_one() {
        val appItems = mutableListOf<AppItem>()
        appItems.add(AppItem(PackageInfo(), "Google Play shangdian", "Google Play shangdian"))

        runBlocking {
            val findApps = AppNameFindUseCase.findApp("pdd", appItems)
            findApps.forEach {
                println("====: ${it.appName}")
            }
        }
    }
}