package com.lib.root.presenter

import com.lib.root.AppPlatform
import com.lib.root.exception.NullReturnValueException

/**
 * RxJava基础回调，自动处理异常以及返回null的情况
 *
 * @author: Admin.
 * @date  : 2019-08-06.
 */
open class ResponseBack<T> : IResponseBack<T> {

    override fun onSuccess(t: T) {
    }

    override fun onNull() {
    }

    override fun onComplete() {
    }

    override fun onError(e: Throwable) {
        if (e is NullReturnValueException) {
            onNull()
        } else {
            AppPlatform.Builder.excpHandler.handlerException(e)
        }
    }

}