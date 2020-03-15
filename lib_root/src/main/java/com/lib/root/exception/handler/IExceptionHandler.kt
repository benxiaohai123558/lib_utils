package com.lib.root.exception.handler

/**
 * 异常处理接口
 *
 * @author: Admin.
 * @date  : 2019-08-06.
 */
interface IExceptionHandler {

    /**
     * 异常处理
     */
    fun handlerException(t: Throwable, upload: Boolean = false)

}