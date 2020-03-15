package com.lib.utils.android.vo

import android.graphics.drawable.Drawable
import java.io.Serializable

/**
 * 封装App信息的Bean类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
data class AppInfoVo(
    val name: String,
    val icon: Drawable,
    val packageName: String,
    val packagePath: String,
    val versionName: String,
    val versionCode: Int,
    val isSystem: Boolean
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}