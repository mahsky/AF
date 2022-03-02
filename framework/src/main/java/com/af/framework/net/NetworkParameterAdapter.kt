package com.af.framework.net

import okhttp3.Request

/**
 * Created by mah on 2022/3/2.
 */
interface NetworkParameterAdapter {
    fun getGetParameter(request: Request): Map<String, String>
    fun getPostQueryParameter(request: Request): Map<String, String>
    fun getPostFieldParameter(request: Request): Map<String, String>
}