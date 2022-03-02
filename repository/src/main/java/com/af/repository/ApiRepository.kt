package com.af.repository

import com.af.framework.net.Repository

/**
 * Created by mah on 2022/2/25.
 */
class ApiRepository : Repository<ApiService>(
    ApiService::class.java,
    "http://retroftcoroutines.free.beeceptor.com/"
) {
    suspend fun getSuccess() = service.getSuccess()
    suspend fun getError() = service.getError()
}