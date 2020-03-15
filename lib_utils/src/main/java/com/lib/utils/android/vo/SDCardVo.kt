package com.lib.utils.android.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * sdcard相关信息
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
@Parcelize
data class SDCardVo(
    val isExist: Boolean,
    val totalBlocks: Long,
    val freeBlocks: Long,
    val availableBlocks: Long,
    val blockByteSize: Long,
    val totalBytes: Long,
    val freeBytes: Long,
    val availableBytes: Long
) : Parcelable