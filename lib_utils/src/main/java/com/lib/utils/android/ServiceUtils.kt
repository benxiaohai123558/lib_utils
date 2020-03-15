package com.lib.utils.android

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection

/**
 * 服务相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object ServiceUtils {
    /**
     * 获取所有运行的服务
     *
     * @param context 上下文
     * @return 服务名集合
     */
    fun getAllRunningService(context: Context): List<ActivityManager.RunningServiceInfo>? {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningServices(0x7FFFFFFF)
    }

    /**
     * 启动服务
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     */
    fun startService(context: Context, className: String) {
        startService(context, Class.forName(className))
    }

    /**
     * 启动服务
     *
     * @param context 上下文
     * @param cls     服务类
     */
    fun startService(context: Context, cls: Class<*>) {
        context.startService(Intent(context, cls))
    }

    /**
     * 停止服务
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     * @return `true`: 停止成功<br></br>`false`: 停止失败
     */
    fun stopService(context: Context, className: String): Boolean {
        return stopService(context, Class.forName(className))
    }

    /**
     * 停止服务
     *
     * @param context 上下文
     * @param cls     服务类
     * @return `true`: 停止成功<br></br>`false`: 停止失败
     */
    fun stopService(context: Context, cls: Class<*>): Boolean {
        return context.stopService(Intent(context, cls))
    }

    /**
     * 绑定服务
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     * @param conn      服务连接对象
     * @param flags     绑定选项
     *
     *  * [Context.BIND_AUTO_CREATE]
     *  * [Context.BIND_DEBUG_UNBIND]
     *  * [Context.BIND_NOT_FOREGROUND]
     *  * [Context.BIND_ABOVE_CLIENT]
     *  * [Context.BIND_ALLOW_OOM_MANAGEMENT]
     *  * [Context.BIND_WAIVE_PRIORITY]
     *
     */
    fun bindService(context: Context, className: String, conn: ServiceConnection, flags: Int) {
        bindService(context, Class.forName(className), conn, flags)
    }

    /**
     * 绑定服务
     *
     * @param context 上下文
     * @param cls     服务类
     * @param conn    服务连接对象
     * @param flags   绑定选项
     *
     *  * [Context.BIND_AUTO_CREATE]
     *  * [Context.BIND_DEBUG_UNBIND]
     *  * [Context.BIND_NOT_FOREGROUND]
     *  * [Context.BIND_ABOVE_CLIENT]
     *  * [Context.BIND_ALLOW_OOM_MANAGEMENT]
     *  * [Context.BIND_WAIVE_PRIORITY]
     *
     */
    fun bindService(context: Context, cls: Class<*>, conn: ServiceConnection, flags: Int) {
        val intent = Intent(context, cls)
        context.bindService(intent, conn, flags)
    }

    /**
     * 解绑服务
     *
     * @param context 上下文
     * @param conn    服务连接对象
     */
    fun unbindService(context: Context, conn: ServiceConnection) {
        context.unbindService(conn)
    }

    /**
     * 判断服务是否运行
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isServiceRunning(context: Context, className: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runServiceInfos = activityManager.getRunningServices(0x7FFFFFFF)
        if (runServiceInfos == null || runServiceInfos.size == 0) return false
        runServiceInfos.forEach {
            if (className == it.service.className) return true
        }
        return false
    }
}