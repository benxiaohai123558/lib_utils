package com.lib.utils.java

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.regex.Pattern
import kotlin.experimental.and
import kotlin.experimental.or

/**
 *
 *
 * @author: Admin.
 * @date  : 2019-08-18.
 */
object NumberUtils {

    private const val ZERO = 10E-6
    private val NF4 = DecimalFormat("0.0##");
    private val NF3 = DecimalFormat("#0.###")
    private val NF2 = DecimalFormat("#,##0.00")
    private val NF = DecimalFormat("#.##")
    private val PATTERN_NUMBER = Pattern.compile("^\\d*[.]?\\d*$")
    private const val PERCENT = 100
    private const val FLOOR = 0.5

    /**
     * @param number
     * @return
     */
    fun format(number: Double?): String {
        return if (number != null) {
            NF.format(number)
        } else "--"
    }

    /**
     * @param number
     * @return
     */
    fun format2(number: Double?): String {
        return if (number != null) {
            NF2.format(number)
        } else "--"
    }

    fun format3(number: Double?): String {
        return if (number == null) {
            "0"
        } else {
            NF3.format(number)
        }
    }

    /**
     * 处理数字的四舍五入. 四对后两位小数进行处理.
     *
     * @param number 要处理的数字.
     * @return 处理结果.
     */
    fun round(number: Double?): Double {
        return Math.floor(number!! * PERCENT + FLOOR) / PERCENT
    }

    /**
     * 保留三位小数，并且四舍五入
     *
     * @param number 要处理的数字.
     * @return 处理结果.
     */
    fun round3(number: Double?): Double {
        if (number == null) {
            return 0.0
        }
        return Math.floor(number * 1000 + FLOOR) / 1000
    }

    /**
     * 处理到整数位的四舍五入.
     *
     * @param number 要处理的数字.
     * @return 处理结果.
     */
    fun roundLong(number: Double?): Double {
        if (number == null) {
            return 0.0
        }
        return Math.floor(number + FLOOR)
    }

    /**
     * 把一个浮点型数据转成有意义的数值,如果是null则转成0.
     *
     * @param source 源浮点型数据.
     * @return 转换后的结果.
     */
    fun nullToZero(source: Double?): Double {
        return source ?: 0.toDouble()
    }

    /**
     * 把一个Short型的数据转成有意义的数值,如果是null则转成0.
     *
     * @param source 源Short型数据.
     * @return 转换后的结果.
     */
    fun nullToZero(source: Short?): Double {
        return source?.toDouble() ?: 0.0
    }

    /**
     * 把一个Integer型的数据转成有意义的数值,如果是null则转成0.
     *
     * @param source 源Short型数据.
     * @return 转换后的结果.
     */
    fun nullToZero(source: Int?): Double {
        return source?.toDouble() ?: 0.0
    }

    /**
     * 对数字的求和.
     *
     * @param a 被加数.
     * @param b 加数.
     * @return 结果.
     */
    fun add(a: Double?, b: Double?): Double {
        return nullToZero(a) + nullToZero(b)
    }

    /**
     * 判断double型数字是否为零(包括正负数).
     *
     * @return 结果.
     */
    fun isZero(d: Double): Boolean {
        return Math.abs(d) < ZERO
    }

    /**
     * 是否是数字.
     *
     * @param source 源数字.
     * @return true, 是正常数字.
     */
    fun isNumber(source: String?): Boolean {
        if (source == null || source.isEmpty()) {
            return false
        }
        val m = PATTERN_NUMBER.matcher(source)
        if (!m.find()) {
            return false
        }
        var first: String? = null
        val split = source.indexOf('.')
        if (split == -1) {
            first = source
        } else {
            first = source.substring(0, split)
        }
        if (first.length == 0) {
            return true
        }
        return !(first.length > 1 && first.startsWith("0"))
    }

    /**
     * 将单位“元”转为“分”
     *
     * @return
     */
    fun yuanToFen(num: Double?): Int {
        var num = num
        if (num == null) {
            num = 0.0
        }
        val num_yuan = round(num)//保留两位小数
        val format = NumberFormat.getInstance()
        // 默认情况下GroupingUsed属性为true,每三位数为一个分组，用英文半角逗号隔开，例如9,333,333
        format.isGroupingUsed = false
        // 设置返回数的小数部分所允许的最大位数
        format.maximumFractionDigits = 0
        return Integer.valueOf(format.format(num_yuan * 100.0))//num_yuan * 100.0的结果小数点后全是0
    }

    /**
     * 检查整数位掩码
     * @param status
     * @param bit
     * @return
     */
    fun checkBitMask(status: Short?, bit: Int): Boolean {
        if (status == null) {
            return false
        }
        val mask = 1 shl bit
        return status and mask.toShort() == mask.toShort()
    }

    /**
     * 翻转整数位掩码
     * @param status
     * @param bit
     * @return
     */
    fun flipBitMask(status: Short, bit: Int): Int {
        val mask = 1 shl bit
        return if (status and mask.toShort() > 0) {
            (status and mask.inv().toShort()).toInt()
        } else {
            (status or mask.toShort()).toInt()
        }
    }

    /**
     * 设置整数位掩码
     * @param status
     * @param bit
     * @return
     */
    fun setBitMask(status: Short, bit: Int): Int {
        return (status or (1 shl bit).toShort()).toInt()
    }

    /**
     * 清除整数位掩码
     * @param status
     * @param bit
     * @return
     */
    fun clearBitMask(status: Short?, bit: Int): Int {
        if (status == null) {
            return 0
        }
        return (status and (1 shl bit).inv().toShort()).toInt()
    }


    /**
     * 将数字转为字符串，格式为：显示1-3位有效小数位，
     * 超过3位将采用"四舍五入"的方式保留3位小数,再按显示规则显示；
     * 无小数位或者小数位末位为0时不显示。
     * @param number double
     * @return String
     */
    fun formatAs1To3Decimal(number: Double?): String {
        return if (number == null) {
            "0"
        } else NF4.format(number)
    }

}