package com.lib.utils.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.lib.utils.android.image.BitmapDecoder
import com.lib.utils.java.CloseUtils
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Asset文件操作工具箱
 *
 * @author: Admin.
 * @date  : 2019-08-03.
 */
object AssetUtils {
    /**
     * 读取给定文件名的文件的内容并转换成字符串
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param charset  转换编码
     * @return 读取结果
     */
    fun getString(context: Context, fileName: String, charset: Charset): String? {
        var inputStream: InputStream? = null
        try {
            inputStream = context.assets.open(fileName)
            val bytes = inputStream.readBytes()
            return String(bytes, 0, bytes.size, charset)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            CloseUtils.closeIO(inputStream)
        }
        return null
    }

    /**
     * 读取给定文件名的文件的内容并转换成字符串
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 读取结果
     */
    fun getString(context: Context, fileName: String): String? {
        var inputStream: InputStream? = null
        return try {
            inputStream = context.assets.open(fileName)
            val bytes = inputStream.readBytes()
            String(bytes, 0, bytes.size, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            CloseUtils.closeIO(inputStream)
        }
    }

    /**
     * 获取位图
     *
     * @param context    上下文
     * @param fileName   文件名称
     * @param outPadding 输出位图的内边距
     * @param options    加载选项
     * @return Bitmap
     */
    fun getBitmap(context: Context, fileName: String, outPadding: Rect, options: BitmapFactory.Options): Bitmap? {
        return BitmapDecoder().decodeFromAssets(context, fileName, outPadding, options)
    }

    /**
     * 获取位图
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @return Bitmap
     */
    fun getBitmap(context: Context, fileName: String): Bitmap? {
        return BitmapDecoder().decodeFromAssets(context, fileName)
    }
}