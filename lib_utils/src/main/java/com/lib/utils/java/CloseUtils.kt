package com.lib.utils.java

import java.io.Closeable

/**
 * 关闭相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-01.
 */
object CloseUtils {

    /**
     * 关闭IO
     */
    fun closeIO(vararg closeables: Closeable?) {
        if (closeables.isNullOrEmpty()) return

        closeables.forEach {
            try {
                it?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}