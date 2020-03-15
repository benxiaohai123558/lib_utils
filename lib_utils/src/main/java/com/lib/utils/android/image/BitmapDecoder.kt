package com.lib.utils.android.image

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.util.TypedValue
import com.lib.utils.java.CloseUtils
import java.io.FileDescriptor
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * 位图解码器
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 * @param maxNumOfPixels 单张图片最大像素数,最大像素数默认为虚拟机可用最大内存的八分之一再除以4，这样可以保证图片不会太大导致内存溢出
 * @param minSlideLength 最小边长，默认为-1
 */
class BitmapDecoder @JvmOverloads constructor(
    private var maxNumOfPixels: Int = (Runtime.getRuntime().maxMemory() / 8 / 4).toInt(),
    private var minSlideLength: Int = -1
) {
    /**
     * 从字节数组中解码位图
     * @param data
     * @param offset
     * @param length
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeByteArray(
        data: ByteArray,
        offset: Int,
        length: Int,
        options: BitmapFactory.Options? = null
    ): Bitmap {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, offset, length, options)
        optionsTemp.inSampleSize = computeSampleSize(optionsTemp, minSlideLength, maxNumOfPixels)
        optionsTemp.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(data, offset, length, options)
    }

    /**
     * 从字节数组中解码位图
     * @param data
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeByteArray(data: ByteArray, options: BitmapFactory.Options? = null): Bitmap {
        return decodeByteArray(data, 0, data.size, options)
    }

    /**
     * 从文件中解码位图
     * @param filePath
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeFile(filePath: String, options: BitmapFactory.Options? = null): Bitmap {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, optionsTemp)
        optionsTemp.inSampleSize = computeSampleSize(optionsTemp, minSlideLength, maxNumOfPixels)
        optionsTemp.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, optionsTemp)
    }

    /**
     * 从文件描述符中解码位图
     * @param fd
     * @param outPadding
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeFileDescriptor(
        fd: FileDescriptor,
        outPadding: Rect? = null,
        options: BitmapFactory.Options? = null
    ): Bitmap {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fd, outPadding, optionsTemp)
        optionsTemp.inSampleSize = computeSampleSize(optionsTemp, minSlideLength, maxNumOfPixels)
        optionsTemp.inJustDecodeBounds = false
        return BitmapFactory.decodeFileDescriptor(fd, outPadding, optionsTemp)
    }

    /**
     * 从资源文件中解码位图
     * @param resource
     * @param id
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeResource(
        resource: Resources,
        id: Int,
        options: BitmapFactory.Options? = null
    ): Bitmap {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resource, id, optionsTemp)
        optionsTemp.inSampleSize = computeSampleSize(optionsTemp, minSlideLength, maxNumOfPixels)
        optionsTemp.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resource, id, optionsTemp)
    }

    /**
     * 从资源文件流中解码位图
     * @param resource
     * @param value
     * @param IInputStreamCreator
     * @param pad
     * @param options
     * @return
     */
    fun decodeResourceStream(
        resource: Resources,
        value: TypedValue,
        IInputStreamCreator: IInputStreamCreator,
        pad: Rect,
        options: BitmapFactory.Options?
    ): Bitmap? {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true

        var inputStream: InputStream? = IInputStreamCreator.onCreateInputStream() ?: return null
        BitmapFactory.decodeResourceStream(resource, value, inputStream, pad, optionsTemp)
        CloseUtils.closeIO(inputStream)

        optionsTemp.inSampleSize = computeSampleSize(optionsTemp, minSlideLength, maxNumOfPixels)
        optionsTemp.inJustDecodeBounds = false

        inputStream = IInputStreamCreator.onCreateInputStream()
        if (inputStream == null) return null
        val bitmap =
            BitmapFactory.decodeResourceStream(resource, value, inputStream, pad, optionsTemp)
        CloseUtils.closeIO(inputStream)

        return bitmap
    }

    /**
     * 从流中解码位图
     * @param IInputStreamCreator
     * @param outPadding
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeStream(
        IInputStreamCreator: IInputStreamCreator,
        outPadding: Rect? = null,
        options: BitmapFactory.Options? = null
    ): Bitmap? {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true

        var inputStream: InputStream? = IInputStreamCreator.onCreateInputStream() ?: return null
        BitmapFactory.decodeStream(inputStream, outPadding, optionsTemp)
        CloseUtils.closeIO(inputStream)

        optionsTemp.inSampleSize = computeSampleSize(optionsTemp, minSlideLength, maxNumOfPixels)
        optionsTemp.inJustDecodeBounds = false

        inputStream = IInputStreamCreator.onCreateInputStream()
        if (inputStream == null) return null
        val bitmap = BitmapFactory.decodeStream(inputStream, outPadding, optionsTemp)
        CloseUtils.closeIO(inputStream)

        return bitmap
    }

    /**
     * 从Assets中解码位图
     * @param context
     * @param fileName
     * @param outPadding
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeFromAssets(
        context: Context,
        fileName: String,
        outPadding: Rect? = null,
        options: BitmapFactory.Options? = null
    ): Bitmap? {
        return decodeStream(object : IInputStreamCreator {
            override fun onCreateInputStream(): InputStream? {
                return try {
                    context.assets.open(fileName)
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

            }
        }, outPadding, options)
    }

    /**
     * 从Uri中解码位图
     * @param context
     * @param uri
     * @param outPadding
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeUri(
        context: Context,
        uri: Uri,
        outPadding: Rect? = null,
        options: BitmapFactory.Options? = null
    ): Bitmap? {
        return decodeStream(object : IInputStreamCreator {
            override fun onCreateInputStream(): InputStream? {
                return try {
                    context.contentResolver.openInputStream(uri)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    null
                }

            }
        }, outPadding, options)
    }

    /**
     * 从字节数组中解码位图的尺寸
     * @param data
     * @param offset
     * @param length
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeSizeFromByteArray(
        data: ByteArray,
        offset: Int,
        length: Int,
        options: BitmapFactory.Options? = null
    ): BitmapFactory.Options {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, offset, length, optionsTemp)
        optionsTemp.inJustDecodeBounds = false
        return optionsTemp
    }

    /**
     * 从文件中解码位图的尺寸
     * @param filePath
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeSizeFromFile(
        filePath: String,
        options: BitmapFactory.Options? = null
    ): BitmapFactory.Options {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, optionsTemp)
        optionsTemp.inJustDecodeBounds = false
        return optionsTemp
    }

    /**
     * 从文件描述符中解码位图的尺寸
     * @param fd
     * @param outPadding
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeSizeFromFileDescriptor(
        fd: FileDescriptor,
        outPadding: Rect? = null,
        options: BitmapFactory.Options? = null
    ): BitmapFactory.Options {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fd, outPadding, optionsTemp)
        optionsTemp.inJustDecodeBounds = false
        return optionsTemp
    }

    /**
     * 从资源文件中解码位图的尺寸
     * @param resource
     * @param id
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeSizeFromResource(
        resource: Resources,
        id: Int,
        options: BitmapFactory.Options? = null
    ): BitmapFactory.Options {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resource, id, optionsTemp)
        optionsTemp.inJustDecodeBounds = false
        return optionsTemp
    }

    /**
     * 从资源流中解码位图的尺寸
     * @param resource
     * @param value
     * @param IInputStreamCreator
     * @param pad
     * @param options
     * @return
     */
    fun decodeSizeFromResourceStream(
        resource: Resources,
        value: TypedValue,
        IInputStreamCreator: IInputStreamCreator,
        pad: Rect,
        options: BitmapFactory.Options?
    ): BitmapFactory.Options {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        val inputStream = IInputStreamCreator.onCreateInputStream() ?: return optionsTemp
        BitmapFactory.decodeResourceStream(resource, value, inputStream, pad, optionsTemp)
        CloseUtils.closeIO(inputStream)

        optionsTemp.inJustDecodeBounds = false
        return optionsTemp
    }

    /**
     * 从流中解码位图的尺寸
     * @param IInputStreamCreator
     * @param outPadding
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeSizeFromStream(
        IInputStreamCreator: IInputStreamCreator,
        outPadding: Rect? = null,
        options: BitmapFactory.Options? = null
    ): BitmapFactory.Options {
        var optionsTemp = options
        if (optionsTemp == null) {
            optionsTemp = BitmapFactory.Options()
        }
        optionsTemp.inJustDecodeBounds = true
        val inputStream = IInputStreamCreator.onCreateInputStream() ?: return optionsTemp
        BitmapFactory.decodeStream(inputStream, outPadding, optionsTemp)
        CloseUtils.closeIO(inputStream)

        optionsTemp.inJustDecodeBounds = false
        return optionsTemp
    }

    /**
     * 从Assets中解码位图的尺寸
     * @param context
     * @param fileName
     * @param outPadding
     * @param options
     * @return
     */
    @JvmOverloads
    fun decodeSizeFromAssets(
        context: Context,
        fileName: String,
        outPadding: Rect? = null,
        options: BitmapFactory.Options? = null
    ): BitmapFactory.Options {
        return decodeSizeFromStream(object : IInputStreamCreator {
            override fun onCreateInputStream(): InputStream? {
                return try {
                    context.assets.open(fileName)
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }

            }
        }, outPadding, options)
    }

    /**
     * 计算合适的缩小倍数，注意在调用此方法之前一定要先通过Options.inJustDecodeBounds属性来获取图片的宽高
     * @param options
     * @param minSideLength 用于指定最小宽度或最小高度
     * @param maxNumOfPixels 最大尺寸，由最大宽高相乘得出
     * @return
     */
    fun computeSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int,
        maxNumOfPixels: Int
    ): Int {
        val initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels)
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }
        return roundedSize
    }

    private fun computeInitialSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int,
        maxNumOfPixels: Int
    ): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val lowerBound =
            if (maxNumOfPixels == -1) 1 else ceil(sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minSideLength == -1) 128 else floor(w / minSideLength).coerceAtMost(
            floor(h / minSideLength)
        ).toInt()
        if (upperBound < lowerBound) {
            return lowerBound
        }

        return if (maxNumOfPixels == -1 && minSideLength == -1) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}