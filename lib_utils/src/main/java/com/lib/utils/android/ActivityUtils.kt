package com.lib.utils.android

import android.content.Intent
import android.os.Bundle
import com.lib.utils.Utils

/**
 * Activity相关工具类
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object ActivityUtils {
    /**
     * 判断是否存在Activity
     *
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isActivityExists(packageName: String, className: String): Boolean {
        val intent = Intent()
        intent.setClassName(packageName, className)
        return !(Utils.instance.context.packageManager.resolveActivity(intent, 0) == null ||
                intent.resolveActivity(Utils.instance.context.packageManager) == null ||
                Utils.instance.context.packageManager.queryIntentActivities(intent, 0).size == 0)
    }

    /**
     * 打开Activity
     *
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     */
    @JvmOverloads
    fun launchActivity(packageName: String, className: String, bundle: Bundle? = null) {
        Utils.instance.context.startActivity(IntentUtils.getComponentIntent(packageName, className, bundle))
    }

    /**
     * 获取launcher activity
     *
     * @param packageName 包名
     * @return launcher activity
     */
    fun getLauncherActivity(packageName: String): String {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pm = Utils.instance.context.packageManager
        val infos = pm.queryIntentActivities(intent, 0)
        if (!infos.isNullOrEmpty()) {
            for (info in infos) {
                if (info.activityInfo.packageName == packageName) {
                    return info.activityInfo.name
                }
            }
        }
        return "no $packageName"
    }
}