package com.lib.utils.android.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 返回的命令结果
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 *
 * @param result 结果码
 * @param successMsg 成功信息
 * @param errorMsg 错误信息
 */
@Parcelize
data class CommandResultVo(val result: Int, val successMsg: String?, val errorMsg: String?) : Parcelable