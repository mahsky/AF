package com.af.framework.net

/**
 * Created by mah on 2022/2/25.
 */
abstract class BaseRepository {
    val retrofit = Network.retrofit
//    val service : T by lazy { retrofit.create<T>() }
//
//    inline fun <reified T> getService(value: T): T = retrofit.create<T>()
}