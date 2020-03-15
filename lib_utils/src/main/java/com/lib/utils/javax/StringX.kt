package com.lib.utils.javax

/**
 * String拓展函数
 *
 * @author: Admin.
 * @date  : 2019-08-01.
 */

fun String.fillZero(digit: Int, stringLength: Int) = this.format("%0" + stringLength + "d", digit)
