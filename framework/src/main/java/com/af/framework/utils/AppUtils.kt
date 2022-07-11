package com.af.framework.utils

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable


/**
 * Created by mah on 2022/7/11.
 */
object AppUtils {

    fun getAppIcon(packageName: String, pm: PackageManager): Drawable? {
        try {
            val info = pm.getApplicationInfo(packageName, 0)
            return info.loadIcon(pm)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}