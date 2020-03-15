package com.lib.root.presenter

/**
 * presenter数据回调
 *
 * @author: Admin.
 * @date  : 2019-08-06.
 */
interface IResponseBack<T> {

    /**
     * 成功
     */
    fun onSuccess(t: T)

    /**
     * 返回null值
     */
    fun onNull()

    /**
     * 完成
     */
    fun onComplete()

    /**
     * 异常
     */
    fun onError(e: Throwable)
}