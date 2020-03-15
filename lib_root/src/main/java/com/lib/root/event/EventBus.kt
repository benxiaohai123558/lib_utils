package com.lib.root.event

/**
 * EventBus封装
 *
 * @author: Admin.
 * @date  : 2019/7/22.
 */
class EventBus private constructor() {

    companion object {
        val instance: EventBus by lazy {
            EventBus()
        }
    }

    /**
     *注册消息
     */
    fun register(subscriber: Any) {
        if (!this.isRegistered(subscriber))
            org.greenrobot.eventbus.EventBus.getDefault().register(subscriber)
    }

    /**
     * 判断消息是否已经注册
     */
    fun isRegistered(subscriber: Any): Boolean {
        return org.greenrobot.eventbus.EventBus.getDefault().isRegistered(subscriber)
    }

    /**
     * 移除注册消息
     */
    fun unregister(subscriber: Any) {
        if (this.isRegistered(subscriber))
            org.greenrobot.eventbus.EventBus.getDefault().unregister(subscriber)
    }

    /**
     * 发送消息
     */
    fun post(event: Any) {
        org.greenrobot.eventbus.EventBus.getDefault().post(event)
    }

    /**
     * 粘性发送消息，既发送消息之后在注册也能收到消息
     */
    fun postSticky(event: Any) {
        org.greenrobot.eventbus.EventBus.getDefault().postSticky(event)
    }

    fun cancelEventDelivery(event: Any) {
        org.greenrobot.eventbus.EventBus.getDefault().cancelEventDelivery(event)
    }

}