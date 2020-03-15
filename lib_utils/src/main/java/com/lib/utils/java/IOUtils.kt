package com.lib.utils.java

import android.annotation.SuppressLint
import android.graphics.Bitmap
import com.lib.utils.java.CloseUtils
import com.lib.utils.java.ConstUtils
import java.io.*
import kotlin.experimental.and

/**
 * 转换相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-02.
 */
object IOUtils {


    private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    /**
     * byteArr转hexString
     *
     * 例如：
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    fun bytes2HexString(bytes: ByteArray?): String? {
        if (bytes == null) return null
        val len = bytes.size
        if (len <= 0) return null
        val ret = CharArray(len shl 1)
        var i = 0
        var j = 0
        while (i < len) {
            ret[j++] = hexDigits[bytes[i].toInt().ushr(4) and 0x0f]
            ret[j++] = hexDigits[(bytes[i] and 0x0f).toInt()]
            i++
        }
        return String(ret)
    }

    /**
     * hexString转byteArr
     *
     * 例如：
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    fun hexString2Bytes(hexString: String): ByteArray? {
        var hexString = hexString
        if (hexString.isNullOrBlank()) return null
        var len = hexString.length
        if (len % 2 != 0) {
            hexString = "0$hexString"
            len += 1
        }
        val hexBytes = hexString.toUpperCase().toCharArray()
        val ret = ByteArray(len shr 1)
        var i = 0
        while (i < len) {
            ret[i shr 1] = (hex2Dec(hexBytes[i]) shl 4 or hex2Dec(hexBytes[i + 1])).toByte()
            i += 2
        }
        return ret
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private fun hex2Dec(hexChar: Char): Int {
        return if (hexChar in '0'..'9') {
            hexChar - '0'
        } else if (hexChar in 'A'..'F') {
            hexChar - 'A' + 10
        } else {
            throw IllegalArgumentException()
        }
    }

    /**
     * charArr转byteArr
     *
     * @param chars 字符数组
     * @return 字节数组
     */
    fun chars2Bytes(chars: CharArray?): ByteArray? {
        if (chars == null || chars.isEmpty()) return null
        val len = chars.size
        val bytes = ByteArray(len)
        for (i in 0 until len) {
            bytes[i] = chars[i].toByte()
        }
        return bytes
    }

    /**
     * byteArr转charArr
     *
     * @param bytes 字节数组
     * @return 字符数组
     */
    fun bytes2Chars(bytes: ByteArray?): CharArray? {
        if (bytes == null) return null
        val len = bytes.size
        if (len <= 0) return null
        val chars = CharArray(len)
        for (i in 0 until len) {
            chars[i] = (bytes[i] and 0xff.toByte()).toChar()
        }
        return chars
    }

    /**
     * 以unit为单位的内存大小转字节数
     *
     * @param memorySize 大小
     * @param unit       单位类型
     *
     *  * [ConstUtils.MemoryUnit.BYTE]: 字节
     *  * [ConstUtils.MemoryUnit.KB]  : 千字节
     *  * [ConstUtils.MemoryUnit.MB]  : 兆
     *  * [ConstUtils.MemoryUnit.GB]  : GB
     *
     * @return 字节数
     */
    fun memorySize2Byte(memorySize: Long, unit: ConstUtils.MemoryUnit): Long {
        if (memorySize < 0) return -1
        return when (unit) {
            ConstUtils.MemoryUnit.BYTE -> memorySize
            ConstUtils.MemoryUnit.KB -> memorySize * ConstUtils.KB
            ConstUtils.MemoryUnit.MB -> memorySize * ConstUtils.MB
            ConstUtils.MemoryUnit.GB -> memorySize * ConstUtils.GB
        }
    }

    /**
     * 字节数转以unit为单位的内存大小
     *
     * @param byteNum 字节数
     * @param unit    单位类型
     *
     *  * [ConstUtils.MemoryUnit.BYTE]: 字节
     *  * [ConstUtils.MemoryUnit.KB]  : 千字节
     *  * [ConstUtils.MemoryUnit.MB]  : 兆
     *  * [ConstUtils.MemoryUnit.GB]  : GB
     *
     * @return 以unit为单位的size
     */
    fun byte2MemorySize(byteNum: Long, unit: ConstUtils.MemoryUnit): Double {
        if (byteNum < 0) return -1.0
        return when (unit) {
            ConstUtils.MemoryUnit.BYTE -> byteNum.toDouble()
            ConstUtils.MemoryUnit.KB -> byteNum.toDouble() / ConstUtils.KB
            ConstUtils.MemoryUnit.MB -> byteNum.toDouble() / ConstUtils.MB
            ConstUtils.MemoryUnit.GB -> byteNum.toDouble() / ConstUtils.GB
        }
    }

    /**
     * 字节数转合适内存大小
     *
     * 保留3位小数
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    @SuppressLint("DefaultLocale")
    fun byte2FitMemorySize(byteNum: Long): String {
        return when {
            byteNum < 0 -> "shouldn't be less than zero!"
            byteNum < ConstUtils.KB -> String.format("%.3fB", byteNum + 0.0005)
            byteNum < ConstUtils.MB -> String.format("%.3fKB", byteNum / ConstUtils.KB + 0.0005)
            byteNum < ConstUtils.GB -> String.format("%.3fMB", byteNum / ConstUtils.MB + 0.0005)
            else -> String.format("%.3fGB", byteNum / ConstUtils.GB + 0.0005)
        }
    }

    /**
     * inputStream转outputStream
     *
     * @param input 输入流
     * @return outputStream子类
     */
    fun input2OutputStream(input: InputStream?): ByteArrayOutputStream? {
        if (input == null) return null
        return try {
            val os = ByteArrayOutputStream()
            os.write(input.readBytes())
            os
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            CloseUtils.closeIO(input)
        }
    }

    /**
     * outputStream转inputStream
     *
     * @param out 输出流
     * @return inputStream子类
     */
    fun output2InputStream(out: OutputStream?): ByteArrayInputStream? {
        return if (out == null) null else ByteArrayInputStream((out as ByteArrayOutputStream).toByteArray())
    }

    /**
     * inputStream转byteArr
     *
     * @param is 输入流
     * @return 字节数组
     */
    fun inputStream2Bytes(input: InputStream?): ByteArray? {
        return if (input == null) null else input2OutputStream(input)!!.toByteArray()
    }

    /**
     * byteArr转inputStream
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    fun bytes2InputStream(bytes: ByteArray?): InputStream? {
        return if (bytes == null || bytes.isEmpty()) null else ByteArrayInputStream(bytes)
    }

    /**
     * outputStream转byteArr
     *
     * @param out 输出流
     * @return 字节数组
     */
    fun outputStream2Bytes(out: OutputStream?): ByteArray? {
        return if (out == null) null else (out as ByteArrayOutputStream).toByteArray()
    }

    /**
     * outputStream转byteArr
     *
     * @param bytes 字节数组
     * @return 字节数组
     */
    fun bytes2OutputStream(bytes: ByteArray?): OutputStream? {
        if (bytes == null || bytes.isEmpty()) return null
        var os: ByteArrayOutputStream? = null
        return try {
            os = ByteArrayOutputStream()
            os.write(bytes)
            os
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            CloseUtils.closeIO(os)
        }
    }

    /**
     * inputStream转string按编码
     *
     * @param is          输入流
     * @param charsetName 编码格式
     * @return 字符串
     */
    fun inputStream2String(input: InputStream?, charsetName: String): String? {
        if (input == null || charsetName.isNullOrBlank()) return null
        return try {
            String(inputStream2Bytes(input)!!, charset(charsetName))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * string转inputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    fun string2InputStream(string: String?, charsetName: String): InputStream? {
        if (string == null || charsetName.isNullOrBlank()) return null
        return try {
            ByteArrayInputStream(string.toByteArray(charset(charsetName)))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * outputStream转string按编码
     *
     * @param out         输出流
     * @param charsetName 编码格式
     * @return 字符串
     */
    fun outputStream2String(out: OutputStream?, charsetName: String): String? {
        if (out == null || charsetName.isNullOrBlank()) return null
        return try {
            String(outputStream2Bytes(out)!!, charset(charsetName))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * string转outputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    fun string2OutputStream(string: String?, charsetName: String): OutputStream? {
        if (string == null || charsetName.isNullOrBlank()) return null
        return try {
            bytes2OutputStream(string.toByteArray(charset(charsetName)))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * bitmap转byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    fun bitmap2Bytes(bitmap: Bitmap?, format: Bitmap.CompressFormat): ByteArray? {
        if (bitmap == null) return null
        val baos = ByteArrayOutputStream()
        bitmap.compress(format, 100, baos)
        return baos.toByteArray()
    }
}