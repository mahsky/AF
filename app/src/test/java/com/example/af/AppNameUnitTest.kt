package com.example.af

import org.junit.Assert
import org.junit.Test

/**
 * Created by mah on 2022/7/11.
 */
class AppNameUnitTest {
    @Test
    fun check_app_name() {
        val names = Data.names.split(",")
        names.forEach {
            println(it)
        }
    }
}