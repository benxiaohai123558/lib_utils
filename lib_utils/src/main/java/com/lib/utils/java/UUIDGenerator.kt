package com.lib.utils.java

import java.util.*

/**
 *
 * 功能：
 * 创建时间：2019-11-18
 * by：huang
 */


/**
 * 构造ID生成器.
 */
class UUIDGenerator {
    private val ID_LENGTH = 36
    private val INT_BIT = 8
    private val STR_BIT = 8
    private val SHORT_BIT = 4
    private val JVM_BIT = 8
    private val HI = 32
    private var counter:Short = 0

    private val DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZaZ".toCharArray()


    private var entityId = "10000000"

    private val JVM = System.currentTimeMillis().ushr(JVM_BIT).toInt()

    companion object {
        val INSTANCE: UUIDGenerator by lazy {
            UUIDGenerator()
        }
    }

    fun getUUID(entityId: String): String {
        val u = UUID.randomUUID()
        return entityId + toIDString(u.mostSignificantBits) + toIDString(u.leastSignificantBits)
    }

    /**
     * 得到当前计数. <br></br>
     * 可防重复.
     *
     * @return 当前计数.
     */
    protected val count: Short
        get() = synchronized(this){
            if (counter < 0) {
                counter = 0
            }
            return counter++
        }
//    fun getCount():Short{
//        synchronized(this){
//            if (counter < 0) {
//                counter = 0
//            }
//            return counter++
//        }
//    }

    private fun toIDString(i: Long): String {
        var i = i
        val buf = "zzzzzzzzzzz".toCharArray()//默认11位,不够用会用z补全
        var cp = 11
        val b: Long = 63//DIGITS 数组的长度,不要越界
        do {
            buf[--cp] = DIGITS[(i and b).toInt()]
            i = i ushr 6
        } while (i != 0L)
        return String(buf)
    }

    /**
     * 得到JVM相关信息. <br></br>
     * 这里是JVM启动时间.
     *
     * @return JVM相关信息.
     */
    protected val jvm: Int
        get() = JVM
    /**
     * 得到高位时间.
     *
     * @return 高位时间.
     */
    protected val hiTime: Short
        get() = System.currentTimeMillis().ushr(HI).toShort()

    /**
     * 得到低位时间.
     *
     * @return 低位时间.
     */
    protected val loTime: Int
        get() = System.currentTimeMillis().toInt()

    fun setEntityId(entityId: String?) {
        if (entityId != null && entityId !== "") {
            if (entityId.length == 8) {
                this.entityId = entityId
            }
        }
    }

    /**
     * 格式化int型数据. <br></br>
     * 占八位.
     *
     * @param intval int型的值.
     * @return 格式化后的字符串.
     */
    protected fun format(intval: Int): String {
        val formatted = Integer.toHexString(intval)
        val buf = StringBuilder("00000000")
        buf.replace(INT_BIT - formatted.length, INT_BIT, formatted)
        return buf.toString()
    }

    /**
     * 格式化short型数据. <br></br>
     * 占四位.
     *
     * @param shortval short型的值.
     * @return 格式化后的字符串.
     */
    protected fun format(shortval: Short): String {
        val formatted = Integer.toHexString(shortval.toInt())
        val buf = StringBuilder("0000")
        buf.replace(SHORT_BIT - formatted.length, SHORT_BIT, formatted)
        return buf.toString()
    }

    /**
     * 得到一个UUID.
     *
     * @return 新的UUID.
     */
    @Synchronized
    fun generate():String{
        return entityId +
                format(jvm) + format(hiTime) +
                format(loTime) + format(count)
    }

    /**
     * 格式化字符串型数据. <br></br>
     * 占八位.
     *
     * @param stringval 字符串型的值.
     * @return 格式化后的结果.
     */
    protected fun format(stringval: String?): String {
        var stringval = stringval
        if (stringval == null) {
            stringval = ""
        }
        stringval = if (stringval.length > STR_BIT)
            stringval.substring(
                stringval.length - STR_BIT, stringval.length
            )
        else
            stringval
        val buf = StringBuilder("00000000")
        buf.replace(STR_BIT - stringval.length, STR_BIT, stringval)
        return buf.toString()
    }
}
