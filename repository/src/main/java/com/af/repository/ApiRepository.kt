package com.af.repository

import com.af.framework.net.NetworkRepository

/**
 * Created by mah on 2022/2/25.
 */
class ApiRepository : NetworkRepository<ApiService>(
    ApiService::class.java,
    "http://retroftcoroutines.free.beeceptor.com/"
) {
    suspend fun getSuccess() = service.getSuccess()
    suspend fun getError() = service.getError()
}