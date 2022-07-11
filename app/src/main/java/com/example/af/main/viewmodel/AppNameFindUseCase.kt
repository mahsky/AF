package com.example.af.main.viewmodel

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import java.util.*


/**
 * Created by mah on 2022/7/11.
 */
object AppNameFindUseCase {
    suspend fun findApp(text: CharSequence?, apps: List<App>): List<App> = withContext(Dispatchers.IO) {
        val tempApps = mutableListOf<App>()
        if (text == null || text.isEmpty()) return@withContext tempApps
        apps.forEach { app ->
            app.sort = 0
            var isContains = true
            text.toString().lowercase(Locale.ENGLISH).toCharArray().forEach { char ->
                if (!app.letterAppName.contains(char)) {
                    isContains = false
                }
                if (isContains) {
                    val index = app.letterAppName.lowercase(Locale.ENGLISH).indexOf(char)
                    if (index != -1) {
                        app.sort = app.sort + index
                    }
                }
            }
            if (isContains) {
                tempApps.add(app)
            }

            tempApps.sortBy { it.sort }
        }
        tempApps
    }

    suspend fun getApps(packageManager: PackageManager): List<App> = withContext(Dispatchers.IO) {
        val apps = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        mutableListOf<App>().apply {
            apps.forEach {
                if (packageManager.getLaunchIntentForPackage(it.packageName) != null) {
                    val appName = it.applicationInfo.loadLabel(packageManager).toString()
                    val appNameSb = StringBuilder()
                    appName.toCharArray().forEach { char ->
                        val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(char, HanyuPinyinOutputFormat().apply {
                            toneType = HanyuPinyinToneType.WITHOUT_TONE
                        })

                        if (pinyinArray == null || pinyinArray.isEmpty()) {
                            appNameSb.append(char)
                        } else {
                            pinyinArray.forEach { c ->
                                appNameSb.append(c)
                            }
                        }
                    }
                    add(App(it, appNameSb.toString(), appName))
                }
            }
        }
    }


}

data class App(val packageInfo: PackageInfo, val letterAppName: String, val appName: String, var sort: Int = 0)