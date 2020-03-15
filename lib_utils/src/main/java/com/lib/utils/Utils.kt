package com.lib.utils

import android.content.Context

/**
 * Utils初始化
 *
 * @author: Admin.
 * @date  : 2019-08-01.
 */
class Utils private constructor() {

    /**
     * 获取上下文对象
     */
    lateinit var context: Context

    /**
     * 获取sp保存的名字
     */
    lateinit var shareName: String

    internal var debug = false

    companion object {
        val instance: Utils by lazy {
            Utils()
        }
    }

    /**
     * 初始化工具类
     *
     * @param context   上下文
     * @param shareName SharedPreferences保存的名字
     */
    fun init(context: Context, shareName: String, debug: Boolean) {
        this.context = context
        this.shareName = shareName
        this.debug = debug
    }
}