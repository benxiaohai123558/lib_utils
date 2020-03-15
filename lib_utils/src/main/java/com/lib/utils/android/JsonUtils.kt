package com.lib.utils.android

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Json解析工具类
 *
 * @author: Admin.
 * @date  : 2019-08-01.
 */
object JsonUtils {

    private var gson: Gson = Gson()

    /**
     * object类型转Json
     */
    fun toJson(any: Any?): String = gson.toJson(any)

    /**
     * json解析获取对象
     */
    fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        if (json.isNullOrBlank()) return null
        return gson.fromJson(json, clazz)
    }

    /**
     * json解析获取对象
     * @param json
     * @param type val fooType = object : TypeToken<T>() {}.type
     */
    fun <T> fromJson(json: String?, type: Type): T? {
        if (json.isNullOrBlank()) return null
        return gson.fromJson(json, type)
    }

}