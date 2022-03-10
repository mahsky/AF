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
        if (Utils.getRawType(type) != NetworkResponse::class.java) return null
        val responseType = getParameterUpperBound(0, type as ParameterizedType)
        val rawResponseType = Utils.getRawType(responseType)
        var checkAnnotationType = responseType
        if (rawResponseType == List::class.java || rawResponseType == Collection::class.java) {
            checkAnnotationType = getParameterUpperBound(0, responseType as ParameterizedType)
        }
        val isNeedCrop = Utils.getRawType(checkAnnotationType).annotations.firstOrNull { it is CropEnvelope } != null
        val dataTypeAdapter = moshi.nextAdapter<Any>(
            this, responseType, annotations
        )
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
        var errorCode = 0
        var errorMsg = ""
        var dataException: Exception? = null
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "data" -> {
                    try {
                        data = dataTypeAdapter.fromJson(reader)
                    } catch (e: Exception) {
                        dataException = e
                    }
                }
                "errorCode" -> errorCode = reader.nextInt()
                "errorMsg" -> errorMsg = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        if (errorCode != 0) {
            return NetworkResponse.BizError(errorCode, errorMsg) as? T
        }

        if (errorCode == 0 && dataException != null) {
            throw dataException
        }

        return if (data == null) null else NetworkResponse.Success(data) as? T
    }

    override fun toJson(writer: JsonWriter, value: T?) {
    }
}

class NetworkResponseTypeAdapter<T>(
    private val outerType: Type,
    private val dataTypeAdapter: JsonAdapter<T>
) : JsonAdapter<T>() {
    override fun fromJson(reader: JsonReader): T? {
        val data: Any? = dataTypeAdapter.fromJson(reader)
        return if (data == null) null else NetworkResponse.Success(data) as? T
    }

    override fun toJson(writer: JsonWriter, value: T?) {
    }
}