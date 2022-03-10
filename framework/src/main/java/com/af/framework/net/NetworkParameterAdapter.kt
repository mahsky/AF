package com.af.framework.net

import okhttp3.Request

/**
 * Created by mah on 2022/3/2.
 */
interface NetworkParameterAdapter {
    /**
     * get url 末尾加参数
     */
    fun getGetParameter(request: Request): Map<String, String>

    /**
     * post url 末尾加参数
     */
    fun getPostQueryParameter(request: Request): Map<String, String>

    /**
     * post FormBody 加参数
     */
    fun getPostFieldParameter(request: Request): Map<String, String>
}