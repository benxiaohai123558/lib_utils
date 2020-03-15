package com.lib.utils.android

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

/**
 * 设置工具箱
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object SettingsUtils {
    /**
     * 获取系统屏幕亮度模式的状态，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC：自动；System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC：手动；默认：System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
     */
    fun getScreenBrightnessModeState(context: Context): Int {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        )
    }

    /**
     * 判断系统屏幕亮度模式是否是自动，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return true：自动；false：手动；默认：true
     */
    fun isScreenBrightnessModeAuto(context: Context): Boolean {
        return getScreenBrightnessModeState(context) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
    }

    /**
     * 设置系统屏幕亮度模式，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @param auto    自动
     * @return 是否设置成功
     */
    fun setScreenBrightnessMode(context: Context, auto: Boolean): Boolean {
        var result = true
        if (isScreenBrightnessModeAuto(context) != auto) {
            result = Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                if (auto) Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC else Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
        }
        return result
    }

    /**
     * 获取系统亮度，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return 亮度，范围是0-255；默认255
     */
    fun getScreenBrightness(context: Context): Int {
        return Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255)
    }

    /**
     * 设置系统亮度（此方法只是更改了系统的亮度属性，并不能看到效果。要想看到效果可以使用setWindowBrightness()方法设置窗口的亮度），需要WRITE_SETTINGS权限
     *
     * @param context          上下文
     * @param screenBrightness 亮度，范围是0-255
     * @return 设置是否成功
     */
    fun setScreenBrightness(context: Context, screenBrightness: Int): Boolean {
        var brightness = screenBrightness
        if (screenBrightness < 1) {
            brightness = 1
        } else if (screenBrightness > 255) {
            brightness = screenBrightness % 255
            if (brightness == 0) {
                brightness = 255
            }
        }
        return Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
    }

    /**
     * 设置给定Activity的窗口的亮度（可以看到效果，但系统的亮度属性不会改变）
     *
     * @param activity         要通过此Activity来设置窗口的亮度
     * @param screenBrightness 亮度，范围是0-255
     */
    fun setWindowBrightness(activity: Activity, screenBrightness: Float) {
        var brightness = screenBrightness
        if (screenBrightness < 1) {
            brightness = 1f
        } else if (screenBrightness > 255) {
            brightness = screenBrightness % 255
            if (brightness == 0f) {
                brightness = 255f
            }
        }
        val window = activity.window
        val localLayoutParams = window.attributes
        localLayoutParams.screenBrightness = brightness / 255
        window.attributes = localLayoutParams
    }

    /**
     * 设置系统亮度并实时可以看到效果，需要WRITE_SETTINGS权限
     *
     * @param activity         要通过此Activity来设置窗口的亮度
     * @param screenBrightness 亮度，范围是0-255
     * @return 设置是否成功
     */
    fun setScreenBrightnessAndApply(activity: Activity, screenBrightness: Int): Boolean {
        var result = setScreenBrightness(activity, screenBrightness)
        if (result) {
            setWindowBrightness(activity, screenBrightness.toFloat())
        }
        return result
    }

    /**
     * 获取屏幕休眠时间，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return 屏幕休眠时间，单位毫秒，默认30000
     */
    fun getScreenDormantTime(context: Context): Int {
        return Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 30000)
    }

    /**
     * 设置屏幕休眠时间，需要WRITE_SETTINGS权限
     *
     * @param context 上下文
     * @return 设置是否成功
     */
    fun setScreenDormantTime(context: Context, millis: Int): Boolean {
        return Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, millis)
    }

    /**
     * 获取飞行模式的状态，需要WRITE_APN_SETTINGS权限
     *
     * @param context 上下文
     * @return 1：打开；0：关闭；默认：关闭
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getAirplaneModeState(context: Context): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.getInt(context.contentResolver, Settings.System.AIRPLANE_MODE_ON, 0)
        } else {
            Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
        }
    }

    /**
     * 判断飞行模式是否打开，需要WRITE_APN_SETTINGS权限
     *
     * @param context 上下文
     * @return true：打开；false：关闭；默认关闭
     */
    fun isAirplaneModeOpen(context: Context): Boolean {
        return getAirplaneModeState(context) == 1
    }

    /**
     * 设置飞行模式的状态，需要WRITE_APN_SETTINGS权限
     *
     * @param context 上下文
     * @param enable  飞行模式的状态
     * @return 设置是否成功
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setAirplaneMode(context: Context, enable: Boolean): Boolean {
        var result = true
        //如果飞行模式当前的状态与要设置的状态不一样
        if (isAirplaneModeOpen(context) != enable) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                result = Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.AIRPLANE_MODE_ON,
                    if (enable) 1 else 0
                )
            } else {
                result = Settings.Global.putInt(
                    context.contentResolver,
                    Settings.Global.AIRPLANE_MODE_ON,
                    if (enable) 1 else 0
                )
            }
            //发送飞行模式已经改变广播
            context.sendBroadcast(Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        }
        return result
    }

    /**
     * 获取蓝牙的状态
     *
     * @return 取值为BluetoothAdapter的四个静态字段：STATE_OFF, STATE_TURNING_OFF, STATE_ON, STATE_TURNING_ON
     * @throws Exception 没有找到蓝牙设备
     */
    @SuppressLint("MissingPermission")
    @Throws(Exception::class)
    fun getBluetoothState(): Int {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.state ?: throw Exception("bluetooth device not found!")
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return true：已经打开或者正在打开；false：已经关闭或者正在关闭
     * @throws Exception 没有找到蓝牙设备
     */
    @Throws(Exception::class)
    fun isBluetoothOpen(): Boolean {
        val bluetoothStateCode = getBluetoothState()
        return bluetoothStateCode == BluetoothAdapter.STATE_ON || bluetoothStateCode == BluetoothAdapter.STATE_TURNING_ON
    }

    /**
     * 设置蓝牙状态
     *
     * @param enable 打开
     * @throws Exception 没有找到蓝牙设备
     */
    @SuppressLint("MissingPermission")
    @Throws(Exception::class)
    fun setBluetooth(enable: Boolean) {
        //如果当前蓝牙的状态与要设置的状态不一样
        if (isBluetoothOpen() != enable) {
            //如果是要打开就打开，否则关闭
            if (enable) {
                BluetoothAdapter.getDefaultAdapter().enable()
            } else {
                BluetoothAdapter.getDefaultAdapter().disable()
            }
        }
    }
}