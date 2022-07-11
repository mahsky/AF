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
    suspend fun findApp(text: CharSequence?, appItems: List<AppItem>): List<AppItem> = withContext(Dispatchers.IO) {
        val tempAppItems = mutableListOf<AppItem>()
        if (text == null || text.isEmpty()) return@withContext tempAppItems
        val textLowercase = text.toString().lowercase(Locale.ENGLISH)

        appItems.forEach { app ->
            app.sort = 0
            var lastIndex = -1
            var isContains = true
            val letterAppName = app.letterAppName.lowercase(Locale.ENGLISH)
            println("check: letterAppName:$letterAppName")
            textLowercase.toCharArray().forEach { char ->
                val index = letterAppName.lowercase(Locale.ENGLISH).indexOf(char, 0.coerceAtLeast(lastIndex + 1))
                if (index != -1) {
                    lastIndex = index
                    app.sort = app.sort + index
                } else {
                    isContains = false
                    return@forEach
                }
            }
            if (isContains) {
                tempAppItems.add(app)
            }

            tempAppItems.sortBy { it.sort }
        }
        tempAppItems
    }

    suspend fun getApps(packageManager: PackageManager): List<AppItem> = withContext(Dispatchers.IO) {
        val apps = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        mutableListOf<AppItem>().apply {
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
                    add(AppItem(it, appNameSb.toString(), appName))
                }
            }
        }
    }


}

data class AppItem(val packageInfo: PackageInfo, val letterAppName: String, val appName: String, var sort: Int = 0)