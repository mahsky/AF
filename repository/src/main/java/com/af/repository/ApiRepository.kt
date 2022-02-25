package com.af.repository

import com.af.framework.net.BaseRepository
import retrofit2.create

/**
 * Created by mah on 2022/2/25.
 */
class ApiRepository : BaseRepository() {
    suspend fun getSuccess() = retrofit.create<ApiService>().getSuccess()
    suspend fun getError() = retrofit.create<ApiService>().getError()
}