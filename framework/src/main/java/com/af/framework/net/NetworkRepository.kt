package com.af.framework.net

/**
 * Created by mah on 2022/2/25.
 */
abstract class NetworkRepository<T>(
    serviceClass: Class<T>,
    baseUrl: String
) {
    val service: T by lazy {
        Network.getRetrofit(baseUrl).create<T>(serviceClass)
    }
}