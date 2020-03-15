package com.lib.root.presenter

import android.content.Context
import com.lib.root.exception.NullReturnValueException
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * 基础数据处理类
 *
 * @author: Admin.
 * @date  : 2019-08-06.
 */
abstract class BasePresenter(val context: Context) {

    private var compositeDisposable: CompositeDisposable? = null

    /**
     * 订阅事件 本地数据库操作，开启io线程
     *
     * @param flowable
     * @param responseBack
     * @param isRemote 是否网络请求，ture表示网络请求，在网络请求调线程池里面运行，false表示耗时操作在io线程池中运行
     * @param isDisposable 是否手动注销事件，只有需要注销的事件才加入事件管理器  false:不需要  true:需要
     */
    protected fun <T> sendRequest(
        flowable: Flowable<T>,
        responseBack: IResponseBack<T>,
        isRemote: Boolean = false,
        isDisposable: Boolean = false
    ) {
        val disposable = subscribe(flowable, responseBack, isRemote)
        if (isDisposable) {
            this.addSubscribe(disposable)
        }
    }

    private fun <T> subscribe(
        flowable: Flowable<T>,
        back: IResponseBack<T>,
        isRemote: Boolean = false
    ): Disposable {
        val onNext = Consumer<T> {
            back.onSuccess(it)
        }
        val throwable = Consumer<Throwable> {
            back.onError(it)
        }
        val onComplete = Action {
            back.onComplete()
        }
        return if (isRemote) {
            flowable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable, onComplete)
        } else {
            flowable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable, onComplete)
        }
    }

    /**
     * 手动注销事件
     */
    protected fun unSubscribe() {
        if (compositeDisposable != null && compositeDisposable?.isDisposed == false) compositeDisposable?.clear()
    }

    /**
     * 订阅事件时，需要手动注销事件时调用
     *
     * @param disposable
     * @return
     */
    private fun addSubscribe(disposable: Disposable?): Boolean? {
        if (disposable == null) return false
        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()
        return compositeDisposable?.add(disposable)
    }

    /**
     * 空值自动处理
     */
    fun <T> checkNull(t: T): T {
        if (t == null) throw NullReturnValueException()
        return t
    }
}