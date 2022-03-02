package com.example.af

import android.app.Application
import com.af.framework.net.Network
import com.af.framework.net.NetworkParameterAdapter
import com.af.framework.net.NetworkResponse
import okhttp3.Request

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

        Network.networkParameterAdapter = object : NetworkParameterAdapter {
            override fun getGetParameter(request: Request): Map<String, String> =
                mutableMapOf<String, String>().apply {
//                    put("os", "android")
                }

            override fun getPostQueryParameter(request: Request): Map<String, String> = mutableMapOf()

            override fun getPostFieldParameter(request: Request): Map<String, String> = mutableMapOf()

        }
    }

}