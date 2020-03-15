package com.lib.utils.delegate

import android.content.Context
import android.content.SharedPreferences
import com.lib.utils.android.JsonUtils
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import com.lib.utils.Utils


/**
 * SharedPreferences委托类
 * 不支持集合
 * @author: Admin.
 * @date  : 2019-08-03.
 */
class Preference<T>(val name: String, private val default: T, private val clazz: Class<T>? = null) :
    ReadWriteProperty<Any?, T> {

    private val prefs: SharedPreferences by lazy {
        Utils.instance.context.getSharedPreferences(Utils.instance.shareName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default, clazz)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }


    private fun <T> findPreference(name: String, default: T, clazz: Class<T>? = null): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> {
                parseType(getString(name, null), clazz)
            }
        } ?: return default
        return res as T
    }

    fun <T> parseType(value: String?, clazz: Class<T>?): T? {
        if (value.isNullOrBlank() || clazz == null) return null
        return JsonUtils.fromJson(value, clazz)
    }


    private fun <T> putPreference(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> {
                putString(name, JsonUtils.toJson(value))
            }
        }.apply()
    }


    /**
     * 删除全部数据
     */
    fun clearPreference() {
        prefs.edit().clear().commit()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(vararg key: String) {
        key.forEach {
            prefs.edit().remove(it).commit()
        }
    }

}