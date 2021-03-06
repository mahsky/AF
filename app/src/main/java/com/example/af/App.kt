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

        /**
         * 统一网络回调、错误处理
         */
        Network.responseListener.add {
            val error = when (it) {
                is NetworkResponse.Success -> "Success"
                is NetworkResponse.BizError -> "BizError ${it.msg}"
                is NetworkResponse.ApiError -> "ApiError"
                is NetworkResponse.NetworkError -> "NetworkError  ${it.error.message}"
                is NetworkResponse.UnknownError -> "UnknownError  ${it.error?.message}"
            }
            println("response listener $error")
        }

        /**
         * 统一加参数
         */
        Network.networkParameterAdapter = object : NetworkParameterAdapter {
            override fun getGetParameter(request: Request): Map<String, String> = mutableMapOf()

            override fun getPostQueryParameter(request: Request): Map<String, String> = mutableMapOf()

            override fun getPostFieldParameter(request: Request): Map<String, String> = mutableMapOf()
        }
    }

}