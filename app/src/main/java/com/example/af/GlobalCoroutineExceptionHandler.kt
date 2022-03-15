package com.example.af

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * Created by mah on 2022/3/15.
 * 协成异常全局handler
 */
class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("--GlobalCoroutineExceptionHandler--")
        exception.printStackTrace()
    }
}