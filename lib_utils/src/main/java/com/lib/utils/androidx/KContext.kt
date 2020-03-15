package com.lib.utils.androidx

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes

/**
 * Context拓展函数
 *
 * @author: Admin.
 * @date  : 2019-08-06.
 */
fun Context.inflateLayout(
    @LayoutRes layoutResId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

@Suppress("DEPRECATION")
fun Context.getColor(@ColorRes resId: Int, context: Context): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(resId, context.theme)
    } else {
        context.resources.getColor(resId)
    }
}