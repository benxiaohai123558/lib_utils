package com.lib.root.exception

/**
 * 业务异常
 *
 * @author: Admin.
 * @date  : 2019-08-06.
 */
class BizException @JvmOverloads constructor(
    val code: String? = null,
    override val message: String?
) : RuntimeException() {
    constructor(code: String? = null, t: Throwable) : this(code, t.message)
}