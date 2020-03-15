package com.lib.utils.java

import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * 压缩相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
class ZipUtils {

    /**
     * 批量压缩文件
     *
     * @param resFiles    待压缩文件集合
     * @param zipFilePath 压缩文件路径
     * @param comment     压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    @JvmOverloads
    fun zipFiles(resFiles: Collection<File>, zipFilePath: String, comment: String? = null): Boolean {
        return zipFiles(resFiles, FileUtils.getFileByPath(zipFilePath), comment)
    }

    /**
     * 批量压缩文件
     *
     * @param resFiles 待压缩文件集合
     * @param zipFile  压缩文件
     * @param comment  压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun zipFiles(resFiles: Collection<File>?, zipFile: File?, comment: String? = null): Boolean {
        if (resFiles == null || zipFile == null) return false
        var zos: ZipOutputStream? = null
        try {
            zos = ZipOutputStream(FileOutputStream(zipFile))
            for (resFile in resFiles) {
                if (!zipFile(resFile, "", zos, comment)) return false
            }
            return true
        } finally {
            if (zos != null) {
                zos.finish()
                CloseUtils.closeIO(zos)
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param resFilePath 待压缩文件路径
     * @param zipFilePath 压缩文件路径
     * @param comment     压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun zipFile(resFilePath: String, zipFilePath: String, comment: String? = null): Boolean {
        return zipFile(FileUtils.getFileByPath(resFilePath), FileUtils.getFileByPath(zipFilePath), comment)
    }

    /**
     * 压缩文件
     *
     * @param resFile 待压缩文件
     * @param zipFile 压缩文件
     * @param comment 压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun zipFile(resFile: File?, zipFile: File?, comment: String? = null): Boolean {
        if (resFile == null || zipFile == null) return false
        var zos: ZipOutputStream? = null
        try {
            zos = ZipOutputStream(FileOutputStream(zipFile))
            return zipFile(resFile, "", zos, comment)
        } finally {
            if (zos != null) {
                CloseUtils.closeIO(zos)
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param resFile  待压缩文件
     * @param rootPath 相对于压缩文件的路径
     * @param zos      压缩文件输出流
     * @param comment  压缩文件的注释
     * @return `true`: 压缩成功<br></br>`false`: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    private fun zipFile(resFile: File, rootPath: String, zos: ZipOutputStream, comment: String?): Boolean {
        var rootPath = rootPath
        rootPath = rootPath + (if (rootPath.isNullOrBlank()) "" else File.separator) + resFile.name
        if (resFile.isDirectory) {
            val fileList = resFile.listFiles()
            // 如果是空文件夹那么创建它，我把'/'换为File.separator测试就不成功，eggPain
            if (fileList == null || fileList.isEmpty()) {
                val entry = ZipEntry("$rootPath/")
                if (!comment.isNullOrBlank()) entry.comment = comment
                zos.putNextEntry(entry)
                zos.closeEntry()
            } else {
                for (file in fileList) {
                    // 如果递归返回false则返回false
                    if (!zipFile(file, rootPath, zos, comment)) return false
                }
            }
        } else {
            var input: InputStream? = null
            try {
                input = BufferedInputStream(FileInputStream(resFile))
                val entry = ZipEntry(rootPath)
                if (!comment.isNullOrBlank()) entry.comment = comment
                zos.putNextEntry(entry)
                val buffer = ByteArray(ConstUtils.KB)
                var len: Int
                do {
                    len = input.read(buffer, 0, ConstUtils.KB)
                    if (len != -1) zos.write(buffer, 0, len) else break
                } while (true)
                zos.closeEntry()
            } finally {
                CloseUtils.closeIO(input)
            }
        }
        return true
    }

    /**
     * 批量解压文件
     *
     * @param zipFiles    压缩文件集合
     * @param destDirPath 目标目录路径
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFiles(zipFiles: Collection<File>, destDirPath: String): Boolean {
        return unzipFiles(zipFiles, FileUtils.getFileByPath(destDirPath))
    }

    /**
     * 批量解压文件
     *
     * @param zipFiles 压缩文件集合
     * @param destDir  目标目录
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFiles(zipFiles: Collection<File>?, destDir: File?): Boolean {
        if (zipFiles == null || destDir == null) return false
        for (zipFile in zipFiles) {
            if (!unzipFile(zipFile, destDir)) return false
        }
        return true
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFile(zipFilePath: String, destDirPath: String): Boolean {
        return unzipFile(FileUtils.getFileByPath(zipFilePath), FileUtils.getFileByPath(destDirPath))
    }

    /**
     * 解压文件
     *
     * @param zipFile 待解压文件
     * @param destDir 目标目录
     * @return `true`: 解压成功<br></br>`false`: 解压失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFile(zipFile: File?, destDir: File?): Boolean {
        return unzipFileByKeyword(zipFile, destDir, null) != null
    }

    /**
     * 解压带有关键字的文件
     *
     * @param zipFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @param keyword     关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFileByKeyword(zipFilePath: String, destDirPath: String, keyword: String): List<File>? {
        return unzipFileByKeyword(FileUtils.getFileByPath(zipFilePath), FileUtils.getFileByPath(destDirPath), keyword)
    }

    /**
     * 解压带有关键字的文件
     *
     * @param zipFile 待解压文件
     * @param destDir 目标目录
     * @param keyword 关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFileByKeyword(zipFile: File?, destDir: File?, keyword: String?): List<File>? {
        if (zipFile == null || destDir == null) return null
        val files = ArrayList<File>()
        val zf = ZipFile(zipFile)
        val entries = zf.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            val entryName = entry.name
            if (keyword.isNullOrBlank() || FileUtils.getFileName(entryName).toLowerCase().contains(keyword?.toLowerCase())) {
                val filePath = destDir.toString() + File.separator + entryName
                val file = File(filePath)
                files.add(file)
                if (entry.isDirectory) {
                    if (!FileUtils.createOrExistsDir(file)) return null
                } else {
                    if (!FileUtils.createOrExistsFile(file)) return null
                    var input: InputStream? = null
                    var out: OutputStream? = null
                    try {
                        input = BufferedInputStream(zf.getInputStream(entry))
                        out = BufferedOutputStream(FileOutputStream(file))
                        val buffer = ByteArray(ConstUtils.KB)
                        var len: Int
                        do {
                            len = input.read(buffer)
                            if (len != -1) out.write(buffer, 0, len) else break
                        } while (true)
                    } finally {
                        CloseUtils.closeIO(input, out)
                    }
                }
            }
        }
        return files
    }

    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun getFilesPath(zipFilePath: String): List<String>? {
        return getFilesPath(FileUtils.getFileByPath(zipFilePath))
    }

    /**
     * 获取压缩文件中的文件路径链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的文件路径链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun getFilesPath(zipFile: File?): List<String>? {
        if (zipFile == null) return null
        val paths = ArrayList<String>()
        val entries = getEntries(zipFile)
        while (entries!!.hasMoreElements()) {
            paths.add((entries.nextElement() as ZipEntry).name)
        }
        return paths
    }

    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的注释链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun getComments(zipFilePath: String): List<String>? {
        return getComments(FileUtils.getFileByPath(zipFilePath))
    }

    /**
     * 获取压缩文件中的注释链表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的注释链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun getComments(zipFile: File?): List<String>? {
        if (zipFile == null) return null
        val comments = ArrayList<String>()
        val entries = getEntries(zipFile)
        while (entries!!.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            comments.add(entry.comment)
        }
        return comments
    }

    /**
     * 获取压缩文件中的文件对象
     *
     * @param zipFilePath 压缩文件路径
     * @return 压缩文件中的文件对象
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun getEntries(zipFilePath: String): Enumeration<*>? {
        return getEntries(FileUtils.getFileByPath(zipFilePath))
    }

    /**
     * 获取压缩文件中的文件对象
     *
     * @param zipFile 压缩文件
     * @return 压缩文件中的文件对象
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun getEntries(zipFile: File?): Enumeration<*>? {
        return if (zipFile == null) null else ZipFile(zipFile).entries()
    }
}