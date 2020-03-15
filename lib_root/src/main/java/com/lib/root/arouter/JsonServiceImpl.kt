package com.lib.root.arouter

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 *
 *
 * @author: Admin.
 * @date  : 2020-02-20.
 */
@Route(path = "/service/json")
class JsonServiceImpl : SerializationService {

    private lateinit var mGson: Gson

    /**
     * Parse json to object
     *
     * USE @parseObject PLEASE
     *
     * @param input json string
     * @param clazz object type
     * @return instance of object
     */
    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        return mGson.fromJson(input, clazz);
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    override fun init(context: Context?) {
        mGson = Gson()
    }

    /**
     * Object to json
     *
     * @param instance obj
     * @return json string
     */
    override fun object2Json(instance: Any?): String {
        return mGson.toJson(instance)
    }

    /**
     * Parse json to object
     *
     * @param input json string
     * @param clazz object type
     * @return instance of object
     */
    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        return mGson.fromJson(input, clazz);
    }
}