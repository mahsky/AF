package com.af.repository

import com.af.framework.net.NetworkResponse
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RepositoryUnitTest {
    @Test
    fun `repository request`() = runBlocking {
        val repository = WanAndroidRepository()
        val response = repository.banner()
        assertTrue(response is NetworkResponse.Success)
        joinAll()
    }

    @Test
    fun `repository crop response`() = runBlocking {
        val repository = WanAndroidRepository()
        val response = repository.cropBanner()
        assertTrue(response is NetworkResponse.Success)
        joinAll()
    }
}