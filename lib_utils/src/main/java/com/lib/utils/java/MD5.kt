package com.lib.utils.java

import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 *
 *
 * @author: Admin.
 * @date  : 2019-08-15.
 */
object MD5 {
    private const val LO_BYTE: Int = 0x0f
    private const val MOVE_BIT: Int = 4
    private const val HI_BYTE: Int = 0xf0
    private val HEX_DIGITS = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

    /**
     * 转换字节数组为16进制字串
     *
     * @param b
     * 字节数组
     * @return 16进制字串
     */

    private fun byteArrayToHexString(b: ByteArray): String {
        val buf = StringBuilder()
        for (aB in b) {
            buf.append(byteToHexString(aB))
        }
        return buf.toString()
    }

    /**
     * 字节转成字符.
     *
     * @param b
     * 原始字节.
     * @return 转换后的字符.
     */
    private fun byteToHexString(b: Byte): String {
        return HEX_DIGITS[(b.toInt() and HI_BYTE) shr MOVE_BIT] + HEX_DIGITS[b.toInt() and LO_BYTE]
    }

    /**
     * 进行加密.
     *
     * @param origin
     * 原始字符串.
     * @return 加密后的结果.
     */
    fun encode(origin: String): String? {
        return encode(origin, null)
    }

    fun encode(origin: String?, context: Context?): String? {
        if (origin == null) {
            throw NullPointerException("origin == null")
        }
        var resultString: String? = null
        var md: MessageDigest? = null
        try {
            md = MessageDigest.getInstance("MD5")
            resultString = byteArrayToHexString(md!!.digest(origin.toByteArray()))
        } catch (e: NoSuchAlgorithmException) {
            //just ignored
            Log.e("MD5Util", "NoSuchAlgorithmException", e)
        }

        return resultString
    }

    /**
     * 对输入流生成校验码.
     * @param input 输入流.
     * @return 生成的校验码.
     */
    fun encode(input: InputStream?): String? {
        if (input == null) {
            throw NullPointerException("origin == null")
        }
        var resultString: String? = null
        try {
            val md = MessageDigest.getInstance("MD5")
            val buffer = ByteArray(1024 * 1024)
            do {
                var len = input.read(buffer)
                if (len > 0) {
                    md.update(buffer, 0, len)
                } else {
                    break
                }
            } while (true)
            resultString = byteArrayToHexString(md.digest())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("MD5Util", "NoSuchAlgorithmException", e)
        } catch (e: IOException) {
            Log.e("MD5Util", "InputStream read error", e)
        }

        return resultString
    }
}