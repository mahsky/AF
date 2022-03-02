package com.af.framework.net

import okhttp3.Request

/**
 * Created by mah on 2022/3/2.
 */
class NetworkParameterAdapterFactory {
    fun get(networkParameterAdapter: NetworkParameterAdapter?): NetworkParameterAdapter {
        return object : NetworkParameterAdapter {
            override fun getGetParameter(request: Request): Map<String, String> =
                networkParameterAdapter?.getGetParameter(request) ?: mutableMapOf()

            override fun getPostQueryParameter(request: Request): Map<String, String> =
                networkParameterAdapter?.getPostQueryParameter(request) ?: mutableMapOf()

            override fun getPostFieldParameter(request: Request): Map<String, String> =
                networkParameterAdapter?.getPostFieldParameter(request) ?: mutableMapOf()
        }
    }
}