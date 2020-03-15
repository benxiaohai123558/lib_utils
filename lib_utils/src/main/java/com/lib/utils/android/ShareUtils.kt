package com.lib.utils.android

import android.content.Context
import android.content.SharedPreferences
import com.lib.utils.Utils
import java.lang.reflect.Type

/**
 * SharedPreferences工具类
 *
 * @author: Admin.
 * @date  : 2019-08-03.
 */
class ShareUtils {

    private var sp: SharedPreferences

    private var editor: SharedPreferences.Editor

    /**
     * SPUtils构造函数
     * 在Application中初始化
     *
     */
    init {
        if (Utils.instance.shareName.isNullOrBlank()) throw NullPointerException("shareName is not null or empty")
        sp = Utils.instance.context.getSharedPreferences(Utils.instance.shareName, Context.MODE_PRIVATE)
        editor = sp.edit()
        editor.apply()
    }

    companion object {
        val instance: ShareUtils by lazy {
            ShareUtils()
        }
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     * @return @return ShareUtils对象
     */
    fun put(key: String, value: String): ShareUtils {
        editor.putString(key, value).apply()
        return this
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`null`
     */
    fun getString(key: String): String? {
        return getString(key, null)
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getString(key: String, defaultValue: String?): String? {
        return sp.getString(key, defaultValue)
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     * @return @return ShareUtils对象
     */
    fun put(key: String, value: Int): ShareUtils {
        editor.putInt(key, value).apply()
        return this
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getInt(key: String): Int {
        return getInt(key, -1)
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getInt(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     * @return @return ShareUtils对象
     */
    fun put(key: String, value: Long): ShareUtils {
        editor.putLong(key, value).apply()
        return this
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getLong(key: String): Long {
        return getLong(key, -1L)
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getLong(key: String, defaultValue: Long): Long {
        return sp.getLong(key, defaultValue)
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     * @return @return ShareUtils对象
     */
    fun put(key: String, value: Float): ShareUtils {
        editor.putFloat(key, value).apply()
        return this
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    fun getFloat(key: String): Float {
        return getFloat(key, -1f)
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getFloat(key: String, defaultValue: Float): Float {
        return sp.getFloat(key, defaultValue)
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     * @return ShareUtils对象
     */
    fun put(key: String, value: Boolean): ShareUtils {
        editor.putBoolean(key, value).apply()
        return this
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值`false`
     */
    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    /**
     * SP中写入Set<String>
     *
     * @param key
     * @param value
     * @return
    <String> */
    fun putStringSet(key: String, value: Set<String>): ShareUtils {
        editor.putStringSet(key, value)
        return this
    }

    /**
     * SP中读取Set<String>
     *
     * @param key          键
     * @param defaultValue 默认字符串集合
     * @return
    </String> */
    fun getStringSet(key: String, defaultValue: Set<String>): Set<String>? {
        return sp.getStringSet(key, defaultValue)
    }

    /**
     * SP中写入对象值obj
     *
     * @param key 键
     * @param obj 值
     * @return ShareUtils对象
     */
    fun putObject(key: String, obj: Any): ShareUtils {
        editor.putString(key, JsonUtils.toJson(obj))
        return this
    }

    /**
     * SP中读取对象
     *
     * @param key  键
     * @param clazz
     * @param <T>
     * @return T
     */
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        return JsonUtils.fromJson(sp.getString(key, null), clazz)
    }

    /**
     * SP中读取对象
     *
     * @param key     键
     * @param typeOfT
     * @param <T>
     * @return
     */
    fun <T> getObject(key: String, typeOfT: Type): T? {
        return JsonUtils.fromJson(sp.getString(key, null), typeOfT)
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    fun getAll(): Map<String, *> {
        return sp.all
    }

    /**
     * SP中移除该key
     *
     * @param keys 键
     */
    fun remove(vararg keys: String) {
        if (keys != null) {
            for (key in keys) {
                editor.remove(key).apply()
            }
        }
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    operator fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    /**
     * SP中清除所有数据
     */
    fun clear() {
        editor.clear().apply()
    }
}