package com.example.af

import com.af.model.AppItem
import com.example.af.main.viewmodel.AppNameFindUseCase
import kotlinx.coroutines.runBlocking
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
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
            appItems.add(AppItem("", it, it))
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
        appItems.add(AppItem("", "Google Play shangdian", "Google Play shangdian"))

        runBlocking {
            val findApps = AppNameFindUseCase.findApp("pdd", appItems)
            findApps.forEach {
                println("====: ${it.appName}")
            }
        }
    }

    @Test
    fun t() {
        val pinyinArray = PinyinHelper.toHanyuPinyinStringArray('相', HanyuPinyinOutputFormat().apply {
            toneType = HanyuPinyinToneType.WITHOUT_TONE
        })?.distinct()

        pinyinArray?.forEach {
            println("====: $it")
        }
    }
}