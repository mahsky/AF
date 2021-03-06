package com.af.repository

import com.af.framework.net.NetworkRepository

/**
 * Created by mah on 2022/3/4.
 */
class WanAndroidRepository : NetworkRepository<WanAndroidService>(
    WanAndroidService::class.java,
    baseUrl = "https://wanandroid.com/"
) {
    suspend fun banner() = service.banner()

    suspend fun cropBanner() = service.cropBanner()
}