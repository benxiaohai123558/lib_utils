package com.lib.utils.android

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 *  键盘相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 *
 * 避免输入法面板遮挡
 * <p>在manifest.xml中activity中设置</p>
 * <p>android:windowSoftInputMode="adjustPan"</p>
 */
object KeyboardUtils {

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    fun hideSoftInput(activity: Activity) {
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * 动态隐藏软键盘
     *
     * @param context 上下文
     * @param view    视图
     */
    fun hideSoftInput(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 动态显示软键盘
     *
     * @param view 视图
     * @param context 上下文对象
     */
    fun showSoftInput(context: Context, view: View) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) imm.showSoftInput(view, 0)
    }

    /**
     * 切换键盘显示与否状态
     * @param context 上下文对象
     */
    fun toggleSoftInput(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}