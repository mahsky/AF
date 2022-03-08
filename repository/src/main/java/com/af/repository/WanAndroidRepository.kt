package com.af.repository

import com.af.framework.net.NetworkRepository

/**
 * Created by mah on 2022/3/4.
 */
class WanAndroidRepository : NetworkRepository<WanAndroidService>(
    WanAndroidService::class.java,
    "https://wanandroid.com/"
) {
    suspend fun wenda() = service.wenda()

    suspend fun banner() = service.banner()
}