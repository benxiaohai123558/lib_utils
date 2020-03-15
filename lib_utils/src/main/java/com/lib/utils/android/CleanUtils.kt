package com.lib.utils.android

import com.lib.utils.Utils
import com.lib.utils.java.FileUtils
import java.io.File

/**
 * 清除相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object CleanUtils {
    /**
     * 清除内部缓存
     *
     * /data/data/com.xxx.xxx/cache
     *
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanInternalCache(): Boolean {
        return FileUtils.deleteFilesInDir(Utils.instance.context.cacheDir)
    }

    /**
     * 清除内部文件
     *
     * /data/data/com.xxx.xxx/files
     *
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanInternalFiles(): Boolean {
        return FileUtils.deleteFilesInDir(Utils.instance.context.filesDir)
    }

    /**
     * 清除内部数据库
     *
     * /data/data/com.xxx.xxx/databases
     *
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanInternalDbs(): Boolean {
        return FileUtils.deleteFilesInDir(Utils.instance.context.filesDir.parent + File.separator + "databases")
    }

    /**
     * 根据名称清除数据库
     *
     * /data/data/com.xxx.xxx/databases/dbName
     *
     * @param dbName 数据库名称
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanInternalDbByName(dbName: String): Boolean {
        return Utils.instance.context.deleteDatabase(dbName)
    }

    /**
     * 清除内部SP
     *
     * /data/data/com.xxx.xxx/shared_prefs
     *
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanInternalSP(): Boolean {
        return FileUtils.deleteFilesInDir(Utils.instance.context.filesDir.parent + File.separator + "shared_prefs")
    }

    /**
     * 清除外部缓存
     *
     * /storage/emulated/0/android/data/com.xxx.xxx/cache
     *
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanExternalCache(): Boolean {
        return SDCardUtils.isSDCardEnable() && FileUtils.deleteFilesInDir(Utils.instance.context.externalCacheDir)
    }

    /**
     * 清除自定义目录下的文件
     *
     * @param dirPath 目录路径
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanCustomCache(dirPath: String): Boolean {
        return FileUtils.deleteFilesInDir(dirPath)
    }

    /**
     * 清除自定义目录下的文件
     *
     * @param dir 目录
     * @return `true`: 清除成功<br></br>`false`: 清除失败
     */
    fun cleanCustomCache(dir: File): Boolean {
        return FileUtils.deleteFilesInDir(dir)
    }
}