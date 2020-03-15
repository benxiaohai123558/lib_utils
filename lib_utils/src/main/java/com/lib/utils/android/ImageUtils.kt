package com.lib.utils.android

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.view.View
import com.lib.utils.java.CloseUtils
import java.io.*

/**
 * 图片相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object ImageUtils {
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


    /**
     * byteArr转bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    fun bytes2Bitmap(bytes: ByteArray?): Bitmap? {
        return if (bytes == null || bytes.isEmpty()) null else BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        return when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            is NinePatchDrawable -> {
                val bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    if (drawable.getOpacity() != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())
                drawable.draw(canvas)
                bitmap
            }
            else -> null
        }
    }

    /**
     * bitmap转drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    fun bitmap2Drawable(res: Resources, bitmap: Bitmap?): Drawable? {
        return if (bitmap == null) null else BitmapDrawable(res, bitmap)
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    fun drawable2Bytes(drawable: Drawable?, format: Bitmap.CompressFormat): ByteArray? {
        return if (drawable == null) null else bitmap2Bytes(drawable2Bitmap(drawable), format)
    }

    /**
     * byteArr转drawable
     *
     * @param res   resources对象
     * @param bytes 字节数组
     * @return drawable
     */
    fun bytes2Drawable(res: Resources?, bytes: ByteArray): Drawable? {
        return if (res == null) null else bitmap2Drawable(res, bytes2Bitmap(bytes))
    }

    /**
     * view转Bitmap
     *
     * @param view 视图
     * @return bitmap
     */
    fun view2Bitmap(view: View?): Bitmap? {
        if (view == null) return null
        val ret = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(ret)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return ret
    }

    /**
     * 计算采样大小
     *
     * @param options   选项
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 采样大小
     */
    private fun calculateInSampleSize(options: BitmapFactory.Options, maxWidth: Int, maxHeight: Int): Int {
        if (maxWidth == 0 || maxHeight == 0) return 1
        var height = options.outHeight
        var width = options.outWidth
        var inSampleSize = 1
        do {
            height = height shr 1
            width = width shr 1
            var result: Boolean = height >= maxHeight && width >= maxWidth
            if (result) {
                inSampleSize = inSampleSize shl 1
            } else {
                break
            }
        } while (true)
        return inSampleSize
    }

    /**
     * 获取bitmap
     *
     * @param file 文件
     * @return bitmap
     */
    fun getBitmap(file: File?): Bitmap? {
        if (file == null) return null
        var input: InputStream? = null
        return try {
            input = BufferedInputStream(FileInputStream(file))
            BitmapFactory.decodeStream(input)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        } finally {
            CloseUtils.closeIO(input)
        }
    }
}