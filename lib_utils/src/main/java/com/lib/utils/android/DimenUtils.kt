package com.lib.utils.android

import android.annotation.SuppressLint
import com.lib.utils.Utils
import com.lib.utils.java.ConstUtils

/**
 * 转换相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object DimenUtils {
    /**
     * 以unit为单位的时间长度转毫秒时间戳
     *
     * @param timeSpan 毫秒时间戳
     * @param unit     单位类型
     *
     *  * [ConstUtils.TimeUnit.MSEC]: 毫秒
     *  * [ConstUtils.TimeUnit.SEC]: 秒
     *  * [ConstUtils.TimeUnit.MIN]: 分
     *  * [ConstUtils.TimeUnit.HOUR]: 小时
     *  * [ConstUtils.TimeUnit.DAY]: 天
     *
     * @return 毫秒时间戳
     */
    fun timeSpan2Millis(timeSpan: Long, unit: ConstUtils.TimeUnit): Long {
        return when (unit) {
            ConstUtils.TimeUnit.MSEC -> timeSpan
            ConstUtils.TimeUnit.SEC -> timeSpan * ConstUtils.SEC
            ConstUtils.TimeUnit.MIN -> timeSpan * ConstUtils.MIN
            ConstUtils.TimeUnit.HOUR -> timeSpan * ConstUtils.HOUR
            ConstUtils.TimeUnit.DAY -> timeSpan * ConstUtils.DAY
        }
    }

    /**
     * 毫秒时间戳转以unit为单位的时间长度
     *
     * @param millis 毫秒时间戳
     * @param unit   单位类型
     *
     *  * [ConstUtils.TimeUnit.MSEC]: 毫秒
     *  * [ConstUtils.TimeUnit.SEC]: 秒
     *  * [ConstUtils.TimeUnit.MIN]: 分
     *  * [ConstUtils.TimeUnit.HOUR]: 小时
     *  * [ConstUtils.TimeUnit.DAY]: 天
     *
     * @return 以unit为单位的时间长度
     */
    fun millis2TimeSpan(millis: Long, unit: ConstUtils.TimeUnit): Long {
        return when (unit) {
            ConstUtils.TimeUnit.MSEC -> millis
            ConstUtils.TimeUnit.SEC -> millis / ConstUtils.SEC
            ConstUtils.TimeUnit.MIN -> millis / ConstUtils.MIN
            ConstUtils.TimeUnit.HOUR -> millis / ConstUtils.HOUR
            ConstUtils.TimeUnit.DAY -> millis / ConstUtils.DAY
        }
    }

    /**
     * 毫秒时间戳转合适时间长度
     *
     * @param millis    毫秒时间戳
     *
     * 小于等于0，返回null
     * @param precision 精度
     *
     *  * precision = 0，返回null
     *  * precision = 1，返回天
     *  * precision = 2，返回天和小时
     *  * precision = 3，返回天、小时和分钟
     *  * precision = 4，返回天、小时、分钟和秒
     *  * precision &gt;= 5，返回天、小时、分钟、秒和毫秒
     *
     * @return 合适时间长度
     */
    @SuppressLint("DefaultLocale")
    fun millis2FitTimeSpan(millis: Long, precision: Int): String? {
        var millisTemp = millis
        var precisionTemp = precision
        if (millisTemp <= 0 || precisionTemp <= 0) return null
        val sb = StringBuilder()
        val units = arrayOf("天", "小时", "分钟", "秒", "毫秒")
        val unitLen = intArrayOf(86400000, 3600000, 60000, 1000, 1)
        precisionTemp = precisionTemp.coerceAtMost(5)
        for (i in 0 until precisionTemp) {
            if (millisTemp >= unitLen[i]) {
                val mode = millisTemp / unitLen[i]
                millisTemp -= mode * unitLen[i]
                sb.append(mode).append(units[i])
            }
        }
        return sb.toString()
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    fun dp2px(dpValue: Int): Float {
        val scale = Utils.instance.context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    fun px2dp(pxValue: Float): Int {
        val scale = Utils.instance.context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    fun sp2px(spValue: Float): Int {
        val fontScale = Utils.instance.context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = Utils.instance.context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}