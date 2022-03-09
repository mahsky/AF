package com.af.framework.net

import com.af.framework.net.Utils.getParameterUpperBound
import com.squareup.moshi.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by mah on 2022/3/4.
 * 序列化对象，自动去信封功能
 */
class NetworkMoshiAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val rawClass = Utils.getRawType(type)
        if (rawClass != NetworkResponse::class.java) return null
        val responseType = getParameterUpperBound(0, type as ParameterizedType)
        val dataTypeAdapter = moshi.nextAdapter<Any>(
            this, responseType, annotations
        )
        val rawResponseType = Utils.getRawType(responseType)
        var checkAnnotationType = responseType

        if (rawResponseType == List::class.java || rawResponseType == Collection::class.java) {
            checkAnnotationType = getParameterUpperBound(0, responseType as ParameterizedType)
        }
        val isNeedCrop = Utils.getRawType(checkAnnotationType).annotations.firstOrNull { it is CropEnvelope } != null

        return if (isNeedCrop) {
            NetworkCropResponseTypeAdapter(responseType, dataTypeAdapter)
        } else {
            NetworkResponseTypeAdapter(responseType, dataTypeAdapter)
        }
    }
}

class NetworkCropResponseTypeAdapter<T>(
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

class NetworkResponseTypeAdapter<T>(
    private val outerType: Type,
    private val dataTypeAdapter: JsonAdapter<T>
) : JsonAdapter<T>() {
    override fun fromJson(reader: JsonReader): T? {
        return NetworkResponse.Success(dataTypeAdapter.fromJson(reader)!!) as T?
    }

    override fun toJson(writer: JsonWriter, value: T?) {
    }

}