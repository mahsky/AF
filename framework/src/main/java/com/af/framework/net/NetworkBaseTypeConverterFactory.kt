package com.af.framework.net

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by mah on 2022/3/11.
 */
class NetworkBaseTypeConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (Utils.getRawType(type) != NetworkResponse::class.java) return null
        val responseType = Utils.getParameterUpperBound(0, type as ParameterizedType)
        if (responseType == String::class.java) return StringConverter()
        return super.responseBodyConverter(type, annotations, retrofit)
    }
}

class StringConverter : Converter<ResponseBody, NetworkResponse<Any, Any>> {
    override fun convert(value: ResponseBody): NetworkResponse<Any, Any> {
        return NetworkResponse.Success(value.string())
    }
}