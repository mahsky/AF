package com.af.framework.net

import com.af.framework.net.Utils.getParameterUpperBound
import com.squareup.moshi.*
import retrofit2.CallAdapter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by mah on 2022/3/4.
 * 序列化对象，自动去信封功能
 */
class NetworkMoshiAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val rawType = type.rawType
        println("--------- $rawType ${Utils.getRawType(type)}")
        if (Utils.getRawType(type) != CropNetworkResponse::class.java) return null

        val responseType = getParameterUpperBound(0, type as ParameterizedType)

        val dataTypeAdapter = moshi.nextAdapter<Any>(
            this, responseType, annotations
        )
        return NetworkResponseTypeAdapter(responseType, dataTypeAdapter)
    }
}

class NetworkResponseTypeAdapter<T>(
    private val outerType: Type,
    private val dataTypeAdapter: JsonAdapter<T>
) : JsonAdapter<T>() {
    override fun fromJson(reader: JsonReader): T? {
        reader.beginObject()
        var data: Any? = null
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "data" -> data = dataTypeAdapter.fromJson(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return NetworkResponse.Success(data!!) as T?
    }

    override fun toJson(writer: JsonWriter, value: T?) {
    }

}