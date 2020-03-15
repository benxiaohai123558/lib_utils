package com.lib.utils.android

import android.annotation.TargetApi
import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.lib.utils.android.vo.SDCardVo
import com.lib.utils.java.CloseUtils
import com.lib.utils.java.IOUtils
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * SD卡相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object SDCardUtils {
    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br></br>false : 不可用
     */
    fun isSDCardEnable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    /**
     * 获取SD卡路径
     *
     * 先用shell，shell失败再普通方法获取，一般是/storage/emulated/0/
     *
     * @return SD卡路径
     */
    fun getSDCardPath(): String? {
        if (!isSDCardEnable()) return null
        val cmd = "cat /proc/mounts"
        val run = Runtime.getRuntime()
        var bufferedReader: BufferedReader? = null
        try {
            val p = run.exec(cmd)
            bufferedReader = BufferedReader(InputStreamReader(BufferedInputStream(p.inputStream)))
            var lineStr: String
            do {
                lineStr = bufferedReader.readLine() ?: break
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    val strArray = lineStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (strArray.size >= 5) {
                        return strArray[1].replace("/.android_secure", "") + File.separator
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    break
                }
            } while (true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            CloseUtils.closeIO(bufferedReader)
        }
        return Environment.getExternalStorageDirectory().path + File.separator
    }

    /**
     * 获取SD卡data路径
     *
     * @return SD卡data路径
     */
    fun getDataPath(): String? {
        return if (!isSDCardEnable()) null else Environment.getExternalStorageDirectory().path + File.separator + "data" + File.separator
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return SD卡剩余空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getFreeSpace(): String? {
        if (!isSDCardEnable()) return null
        val stat = StatFs(getSDCardPath())
        val blockSize: Long
        val availableBlocks: Long
        availableBlocks = stat.availableBlocksLong
        blockSize = stat.blockSizeLong
        return IOUtils.byte2FitMemorySize(availableBlocks * blockSize)
    }

    /**
     * 获取SD卡信息
     *
     * @return SDCardInfo
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getSDCardInfo(): String? {
        if (!isSDCardEnable()) return null
        val sf = StatFs(Environment.getExternalStorageDirectory().path)
        val sd = SDCardVo(
            isExist = true,
            totalBlocks = sf.blockCountLong,
            blockByteSize = sf.blockSizeLong,
            availableBlocks = sf.availableBlocksLong,
            availableBytes = sf.availableBytes,
            freeBlocks = sf.freeBlocksLong,
            freeBytes = sf.freeBytes,
            totalBytes = sf.totalBytes
        )
        return sd.toString()
    }
}