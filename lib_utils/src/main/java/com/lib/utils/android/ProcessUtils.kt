package com.lib.utils.android

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import com.lib.utils.Utils
import java.util.*

/**
 * 进程相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object ProcessUtils {

    /**
     * 获取前台线程包名
     *
     * 当不是查看当前App，且SDK大于21时，
     * 需添加权限 `<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"/>`
     *
     * @return 前台应用包名
     */
    fun getForegroundProcessName(): String? {
        val context = Utils.instance.context
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE)
        if (manager is ActivityManager) {
            val infos = manager.runningAppProcesses
            if (infos != null && infos.size != 0) {
                for (info in infos) {
                    if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return info.processName
                    }
                }
            }
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                val packageManager = context.packageManager
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                println(list)
                if (list.size > 0) {// 有"有权查看使用权限的应用"选项
                    try {
                        val info = packageManager.getApplicationInfo(context.packageName, 0)
                        val aom = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                        if (aom.checkOpNoThrow(
                                AppOpsManager.OPSTR_GET_USAGE_STATS,
                                info.uid,
                                info.packageName
                            ) != AppOpsManager.MODE_ALLOWED
                        ) {
                            context.startActivity(intent)
                        }
                        if (aom.checkOpNoThrow(
                                AppOpsManager.OPSTR_GET_USAGE_STATS,
                                info.uid, info.packageName
                            ) != AppOpsManager.MODE_ALLOWED
                        ) {
                            if (Utils.instance.debug) {

                            }
                            this.logger("getForegroundProcessName", "没有打开\"有权查看使用权限的应用\"选项");
                            return null
                        }
                        val usageStatsManager =
                            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                        val endTime = System.currentTimeMillis()
                        val beginTime = endTime - 86400000 * 7
                        val usageStatses =
                            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime, endTime)
                        if (usageStatses == null || usageStatses.isEmpty()) return null
                        var recentStats: UsageStats? = null
                        for (usageStats in usageStatses) {
                            if (recentStats == null || usageStats.lastTimeUsed > recentStats.lastTimeUsed) {
                                recentStats = usageStats
                            }
                        }
                        return recentStats?.packageName
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }

                } else {
                    this.logger("getForegroundProcessName", "无\"有权查看使用权限的应用\"选项")
                }
            }
        }
        return null
    }

    /**
     * 获取后台服务进程
     *
     * 需添加权限 `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>`
     *
     * @return 后台服务进程
     */
    @SuppressLint("MissingPermission")
    fun getAllBackgroundProcesses(context: Context): Set<String> {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = am.runningAppProcesses
        val set = HashSet<String>()
        for (info in infos) {
            Collections.addAll(set, *info.pkgList)
        }
        return set
    }

    /**
     * 杀死所有的后台服务进程
     *
     * 需添加权限 `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>`
     *
     * @return 被暂时杀死的服务集合
     */
    @SuppressLint("MissingPermission")
    fun killAllBackgroundProcesses(context: Context): Set<String> {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var infos: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
        val set = HashSet<String>()
        for (info in infos) {
            for (pkg in info.pkgList) {
                am.killBackgroundProcesses(pkg)
                set.add(pkg)
            }
        }
        infos = am.runningAppProcesses
        for (info in infos) {
            for (pkg in info.pkgList) {
                set.remove(pkg)
            }
        }
        return set
    }

    /**
     * 杀死后台服务进程
     *
     * 需添加权限 `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>`
     *
     * @param packageName 包名
     * @return `true`: 杀死成功<br></br>`false`: 杀死失败
     */
    @SuppressLint("MissingPermission")
    fun killBackgroundProcesses(packageName: String, context: Context): Boolean {
        if (packageName.isNullOrBlank()) return false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var infos: List<ActivityManager.RunningAppProcessInfo>? = am.runningAppProcesses
        if (infos == null || infos.isEmpty()) return true
        for (info in infos) {
            if (Arrays.asList(*info.pkgList).contains(packageName)) {
                am.killBackgroundProcesses(packageName)
            }
        }
        infos = am.runningAppProcesses
        if (infos == null || infos.isEmpty()) return true
        for (info in infos) {
            if (Arrays.asList(*info.pkgList).contains(packageName)) {
                return false
            }
        }
        return true
    }

    private fun logger(funName: String, msg: String) {
        if (Utils.instance.debug) {
            Log.e("${this.javaClass.name} -> $funName()", msg)
        }
    }
}