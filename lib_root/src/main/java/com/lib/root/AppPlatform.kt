package com.lib.root

import com.lib.root.exception.handler.IExceptionHandler

/**
 * app平台配置
 *
 * @author: Admin.
 * @date  : 2019-08-06.
 */
class AppPlatform(val mBuilder: Builder) {

    object Builder {
        /**
         * 是否测试环境
         */
        var debug: Boolean = false
            private set
        /**
         * app版本名字
         */
        lateinit var versionName: String
            private set

        /**
         * app版本号
         */
        var versionCode: Int = 0
            private set

        /**
         * app异常处理类
         */
        lateinit var excpHandler: IExceptionHandler
            private set

        fun setDebug(debug: Boolean): Builder {
            Builder.debug = debug
            return this
        }

        fun setVersionName(versionName: String): Builder {
            Builder.versionName = versionName
            return this
        }

        fun setVersionCode(versionCode: Int): Builder {
            Builder.versionCode = versionCode
            return this
        }

        fun setExcpHandler(excpHandler: IExceptionHandler): Builder {
            Builder.excpHandler = excpHandler
            return this
        }

        fun builder(): AppPlatform {
            return AppPlatform(this)
        }
    }

}