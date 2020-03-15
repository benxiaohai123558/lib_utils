package com.lib.utils.android

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.lib.utils.Utils
import com.lib.utils.android.vo.AppInfoVo
import com.lib.utils.java.FileUtils
import java.io.File
import java.util.*

/**
 * App相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object AppUtils {

    /**
     * 安装App(支持6.0)
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    fun installApp(context: Context, filePath: String) {
        installApp(context, FileUtils.getFileByPath(filePath))
    }

    /**
     * 安装App（支持6.0）
     *
     * @param context 上下文
     * @param file    文件
     */
    fun installApp(context: Context, file: File?) {
        if (!FileUtils.isFileExists(file)) return
        context.startActivity(IntentUtils.getInstallAppIntent(file))
    }

    /**
     * 安装App（支持6.0）
     *
     * @param activity    activity
     * @param filePath    文件路径
     * @param requestCode 请求值
     */
    fun installApp(activity: Activity, filePath: String, requestCode: Int) {
        installApp(activity, FileUtils.getFileByPath(filePath), requestCode)
    }

    /**
     * 安装App(支持6.0)
     *
     * @param activity    activity
     * @param file        文件
     * @param requestCode 请求值
     */
    fun installApp(activity: Activity, file: File?, requestCode: Int) {
        if (!FileUtils.isFileExists(file)) return
        activity.startActivityForResult(IntentUtils.getInstallAppIntent(file), requestCode)
    }

    /**
     * 静默安装App
     *
     * 非root需添加权限 `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param filePath 文件路径
     * @return `true`: 安装成功<br></br>`false`: 安装失败
     */
    fun installAppSilent(filePath: String): Boolean {
        val file = FileUtils.getFileByPath(filePath)
        if (!FileUtils.isFileExists(file)) return false
        val command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install $filePath"
        val (_, successMsg) = ShellUtils.execCmd(
            command,
            !isSystemApp(Utils.instance.context),
            true
        )
        return successMsg != null && successMsg.toLowerCase().contains("success")
    }

    /**
     * 卸载App
     *
     * @param context     上下文
     * @param packageName 包名
     */
    fun uninstallApp(context: Context, packageName: String?) {
        if (packageName.isNullOrBlank()) return
        context.startActivity(IntentUtils.getUninstallAppIntent(packageName))
    }

    /**
     * 卸载App
     *
     * @param activity    activity
     * @param packageName 包名
     * @param requestCode 请求值
     */
    fun uninstallApp(activity: Activity, packageName: String?, requestCode: Int) {
        if (packageName.isNullOrBlank()) return
        activity.startActivityForResult(IntentUtils.getUninstallAppIntent(packageName), requestCode)
    }

    /**
     * 静默卸载App
     *
     * 非root需添加权限 `<uses-permission android:name="android.permission.DELETE_PACKAGES" />`
     *
     * @param context     上下文
     * @param packageName 包名
     * @param isKeepData  是否保留数据
     * @return `true`: 卸载成功<br></br>`false`: 卸载成功
     */
    fun uninstallAppSilent(context: Context, packageName: String?, isKeepData: Boolean): Boolean {
        if (packageName.isNullOrBlank()) return false
        val command =
            "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (if (isKeepData) "-k " else "") + packageName
        val (_, successMsg) = ShellUtils.execCmd(command, !isSystemApp(context), true)
        return successMsg != null && successMsg.toLowerCase().contains("success")
    }


    /**
     * 判断App是否有root权限
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isAppRoot(): Boolean {
        val result = ShellUtils.execCmd("echo root", true)
        if (result.result == 0) {
            return true
        }
        if (result.errorMsg != null) {
            Log.d("isAppRoot", result.errorMsg);
        }
        return false
    }

    /**
     * 打开App
     *
     * @param packageName 包名
     */
    fun launchApp(packageName: String?) {
        if (packageName.isNullOrBlank()) return
        Utils.instance.context.startActivity(IntentUtils.getLaunchAppIntent(packageName))
    }

    /**
     * 打开App
     *
     * @param activity    activity
     * @param packageName 包名
     * @param requestCode 请求值
     */
    fun launchApp(activity: Activity, packageName: String?, requestCode: Int) {
        if (packageName.isNullOrBlank()) return
        activity.startActivityForResult(IntentUtils.getLaunchAppIntent(packageName), requestCode)
    }

    /**
     * 获取App包名
     *
     * @param context 上下文
     * @return App包名
     */
    fun getAppPackageName(context: Context): String {
        return context.packageName
    }

    /**
     * 获取App具体设置
     *
     * @param context 上下文
     */
    fun getAppDetailsSettings(context: Context) {
        getAppDetailsSettings(context, context.packageName)
    }

    /**
     * 获取App具体设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    fun getAppDetailsSettings(context: Context, packageName: String?) {
        if (packageName.isNullOrBlank()) return
        context.startActivity(IntentUtils.getAppDetailsSettingsIntent(packageName))
    }

    /**
     * 获取App名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App名称
     */
    @JvmOverloads
    fun getAppName(context: Context, packageName: String = context.packageName): String? {
        if (packageName.isNullOrBlank()) return null
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadLabel(pm)?.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取App图标
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App图标
     */
    @JvmOverloads
    fun getAppIcon(context: Context, packageName: String = context.packageName): Drawable? {
        if (packageName.isNullOrBlank()) return null
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.loadIcon(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取App路径
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App路径
     */
    @JvmOverloads
    fun getAppPath(context: Context, packageName: String = context.packageName): String? {
        if (packageName.isNullOrBlank()) return null
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    @JvmOverloads
    fun getAppVersionName(context: Context, packageName: String = context.packageName): String? {
        if (packageName.isNullOrBlank()) return null
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取App版本码
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本码
     */
    @Suppress("DEPRECATION")
    @JvmOverloads
    fun getAppVersionCode(context: Context, packageName: String = context.packageName): Int {
        if (packageName.isBlank()) return -1
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pi?.longVersionCode?.toInt() ?: 1
            } else {
                pi?.versionCode ?: 1
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }

    }

    /**
     * 判断App是否是系统应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmOverloads
    fun isSystemApp(context: Context, packageName: String = context.packageName): Boolean {
        if (packageName.isNullOrBlank()) return false
        return try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            ai != null && ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 判断App是否是Debug版本
     *
     * @param context     上下文
     * @param packageName 包名
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmOverloads
    fun isAppDebug(context: Context, packageName: String = context.packageName): Boolean {
        if (packageName.isNullOrBlank()) return false
        return try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 判断App是否处于前台
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isAppForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = manager.runningAppProcesses
        if (infos == null || infos.size == 0) return false
        for (info in infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName == context.packageName
            }
        }
        return false
    }

    /**
     * 判断App是否处于前台
     *
     * 当不是查看当前App，且SDK大于21时，
     * 需添加权限 `<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"/>`
     *
     * @param context     上下文
     * @param packageName 包名
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isAppForeground(packageName: String?): Boolean {
        return !packageName.isNullOrBlank() && packageName == ProcessUtils.getForegroundProcessName()
    }


    /**
     * 获取App信息
     *
     * AppInfo（名称，图标，包名，版本号，版本Code，是否系统应用）
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 当前应用的AppInfo
     */
    @JvmOverloads
    fun getAppInfo(context: Context, packageName: String = context.packageName): AppInfoVo? {
        return try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            getBean(pm, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 得到AppInfo的Bean
     *
     * @param pm 包的管理
     * @param pi 包的信息
     * @return AppInfo类
     */
    @Suppress("DEPRECATION")
    private fun getBean(pm: PackageManager?, pi: PackageInfo?): AppInfoVo? {
        if (pm == null || pi == null) return null
        val ai = pi.applicationInfo
        val packageName = pi.packageName
        val name = ai.loadLabel(pm).toString()
        val icon = ai.loadIcon(pm)
        val packagePath = ai.sourceDir
        val versionName = pi.versionName
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pi?.longVersionCode?.toInt()
        } else {
            pi?.versionCode
        }
        val isSystem = ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
        return AppInfoVo(name, icon, packageName, packagePath, versionName, versionCode, isSystem)
    }

    /**
     * 获取所有已安装App信息
     *
     * [.getBean]（名称，图标，包名，包路径，版本号，版本Code，是否系统应用）
     *
     * 依赖上面的getBean方法
     *
     * @param context 上下文
     * @return 所有已安装的AppInfo列表
     */
    fun getAppsInfo(context: Context): List<AppInfoVo> {
        val list = ArrayList<AppInfoVo>()
        val pm = context.packageManager
        // 获取系统中安装的所有软件信息
        val installedPackages = pm.getInstalledPackages(0)
        if (!installedPackages.isNullOrEmpty()) {
            for (pi in installedPackages) {
                val ai = getBean(pm, pi) ?: continue
                list.add(ai)
            }
        }
        return list
    }

    /**
     * 清除App所有数据
     *
     * @param context  上下文
     * @param dirPaths 目录路径
     * @return `true`: 成功<br></br>`false`: 失败
     */
    fun cleanAppData(vararg dirPaths: String): Boolean {
        val dirs = arrayOfNulls<File>(dirPaths.size)
        var i = 0
        for (dirPath in dirPaths) {
            dirs[i++] = File(dirPath)
        }
        return cleanAppData(dirs)
    }

    /**
     * 清除App所有数据
     *
     * @param dirs 目录
     * @return `true`: 成功<br></br>`false`: 失败
     */
    fun cleanAppData(dirs: Array<File?>): Boolean {
        var isSuccess = CleanUtils.cleanInternalCache()
        isSuccess = isSuccess and CleanUtils.cleanInternalDbs()
        isSuccess = isSuccess and CleanUtils.cleanInternalSP()
        isSuccess = isSuccess and CleanUtils.cleanInternalFiles()
        isSuccess = isSuccess and CleanUtils.cleanExternalCache()
        for (dir in dirs) {
            if (dir != null) isSuccess = isSuccess and CleanUtils.cleanCustomCache(dir)

        }
        return isSuccess
    }

    /**
     * 获取Manifest Meta Data
     *
     * @param context
     * @param metaKey
     * @return
     */
    fun getMetaData(context: Context, metaKey: String): String {
        val name = context.packageName
        val appInfo: ApplicationInfo
        var msg: String? = ""
        try {
            appInfo = context.packageManager.getApplicationInfo(name, PackageManager.GET_META_DATA)
            msg = appInfo.metaData.getString(metaKey)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return if (msg.isNullOrBlank()) {
            ""
        } else msg

    }

    /**
     * 获得渠道号
     *
     * @param context
     * @param channelKey
     * @return
     */
    fun getChannelNo(context: Context, channelKey: String): String {
        return getMetaData(context, channelKey)
    }
}