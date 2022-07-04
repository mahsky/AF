package com.example.af

import com.af.repository.WanAndroidService
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun t() {
        val a = 0
        println("=================" + WanAndroidService::javaClass.typeParameters.size)
        WanAndroidService::javaClass.typeParameters.forEach {
            println(it)
        }
    }
}