package com.lib.root.arouter

import android.app.Activity
import android.app.Application
import android.net.Uri
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback

/**
 * ARouter封装
 *
 * @author: Admin
 * @date  : 2019/7/29.
 */
class ARouter private constructor() {

    private var fgmentCache: MutableMap<String, Fragment> = mutableMapOf<String, Fragment>()

    companion object {
        val instance: ARouter by lazy {
            ARouter()
        }

        //初始化ARouter
        fun init(application: Application, debug: Boolean) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            if (debug) {
                // 打印日志
                com.alibaba.android.arouter.launcher.ARouter.openLog()
                // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                com.alibaba.android.arouter.launcher.ARouter.openDebug()
            }
            com.alibaba.android.arouter.launcher.ARouter.init(application)
        }
    }

    /**
     * Navigation to the route with path in postcard.
     * No param, will be use application context.
     */
    fun build(
        path: String,
        mContext: Activity? = null,
        callback: NavigationCallback? = null
    ): Any? =
        com.alibaba.android.arouter.launcher.ARouter.getInstance().build(path).navigation(
            mContext,
            callback
        )

    fun build(
        path: String,
        mContext: Activity? = null,
        callback: NavigationCallback? = null,
        withObject: MutableMap<String?, Any?>? = null
    ): Any? {
        val postcard = com.alibaba.android.arouter.launcher.ARouter.getInstance().build(path)
        if (!withObject.isNullOrEmpty()) {
            withObject.keys.forEach {
                when (val value = withObject[it]) {
                    is String -> {
                        postcard.withString(it, value)
                    }
                    is Int -> {
                        postcard.withInt(it, value)
                    }
                    is Double -> {
                        postcard.withDouble(it, value)
                    }
                    else -> {
                        postcard.withObject(it, withObject[it])
                    }
                }
            }
        }
        return postcard.navigation(
            mContext,
            callback
        )
    }

    fun build(
        path: String,
        requestCode: Int,
        mContext: Activity,
        callback: NavigationCallback? = null
    ): Any? =
        com.alibaba.android.arouter.launcher.ARouter.getInstance().build(path).navigation(
            mContext,
            requestCode,
            callback
        )

    /**
     * Build postcard by uri
     */
    fun build(uri: Uri): Postcard? =
        com.alibaba.android.arouter.launcher.ARouter.getInstance().build(uri)

    /**
     * Inject params and services.
     */
    fun inject(thiz: Any) {
        com.alibaba.android.arouter.launcher.ARouter.getInstance().inject(thiz)
    }

    /**
     * 获取fragment
     * 如果缓存存在，从缓存获取，防止fragment重复创建
     */
    fun getFragment(path: String): Fragment {
        var fragment: Fragment? = null
        if (fgmentCache.containsKey(path)) {
            fragment = fgmentCache[path]
        }
        if (fragment == null) {
            return com.alibaba.android.arouter.launcher.ARouter.getInstance().build(path).navigation() as Fragment
        }
        return fragment
    }

    fun destroy() {
        com.alibaba.android.arouter.launcher.ARouter.getInstance().destroy()
    }

}