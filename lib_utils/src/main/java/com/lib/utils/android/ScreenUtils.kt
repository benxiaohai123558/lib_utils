package com.lib.utils.android

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Surface
import android.view.WindowManager
import com.lib.utils.Utils

/**
 * 屏幕相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object ScreenUtils {
    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    fun getScreenWidth(): Int {
        val windowManager =
            Utils.instance.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()// 创建了一张白纸
        windowManager.defaultDisplay.getMetrics(dm)// 给白纸设置宽高
        return dm.widthPixels
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    fun getScreenHeight(): Int {
        val windowManager =
            Utils.instance.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()// 创建了一张白纸
        windowManager.defaultDisplay.getMetrics(dm)// 给白纸设置宽高
        return dm.heightPixels
    }

    /**
     * 获取屏幕密度
     */
    fun getDensityDpi(): Int {
        val metric = Utils.instance.context.resources.displayMetrics
        return metric.densityDpi
    }

    /**
     * 设置屏幕为横屏
     *
     * 还有一种就是在Activity中加属性android:screenOrientation="landscape"
     *
     * 不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
     *
     * 设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
     *
     * 设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法
     *
     * @param activity activity
     */
    fun setLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    fun setPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 判断是否横屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isLandscape(): Boolean {
        return Utils.instance.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 判断是否竖屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isPortrait(): Boolean {
        return Utils.instance.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    fun getScreenRotation(activity: Activity): Int {
        return when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    fun captureWithStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels)
        view.destroyDrawingCache()
        return ret
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    fun captureWithoutStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val statusBarHeight = BarUtils.getStatusBarHeight(activity)
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val ret = Bitmap.createBitmap(
            bmp,
            0,
            statusBarHeight,
            dm.widthPixels,
            dm.heightPixels - statusBarHeight
        )
        view.destroyDrawingCache()
        return ret
    }

    /**
     * 判断是否锁屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isScreenLock(): Boolean {
        val km =
            Utils.instance.context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return km.inKeyguardRestrictedInputMode()
    }

    /**
     * 设置进入休眠时长
     *
     * 需添加权限 `<uses-permission android:name="android.permission.WRITE_SETTINGS" />`
     *
     * @param duration 时长
     */
    fun setSleepDuration(duration: Int) {
        Settings.System.putInt(
            Utils.instance.context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            duration
        )
    }

    /**
     * 获取进入休眠时长
     *
     * @return 进入休眠时长，报错返回-123
     */
    @Throws(Exception::class)
    fun getSleepDuration(): Int {
        return Settings.System.getInt(
            Utils.instance.context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT
        )
    }
}