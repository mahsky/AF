package com.af.repository

import com.af.framework.net.NetworkRepository

/**
 * Created by mah on 2022/3/11.
 */
class LianJiaRepository : NetworkRepository<LianJIaService>(
    LianJIaService::class.java,
    baseUrl = "https://bj.lianjia.com"
) {
    suspend fun dongYiQu(id: String) = service.dongYiQu(id)
}