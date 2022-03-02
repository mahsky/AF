package com.example.af

import android.app.Application
import com.af.framework.net.Network
import com.af.framework.net.NetworkResponse

/**
 * Created by mah on 2022/2/23.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Network.responseListener.add {
            val error = when (it) {
                is NetworkResponse.Success -> "Success"
                is NetworkResponse.ApiError -> "ApiError"
                is NetworkResponse.NetworkError -> "NetworkError"
                is NetworkResponse.UnknownError -> "UnknownError"
            }
            println("response listener $error")
        }
    }

}