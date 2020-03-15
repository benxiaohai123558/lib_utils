package com.lib.utils.android.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 手机状态信息
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 *
 * @param deviceId(IMEI) = 99000311726612 IMEI码
 * @param deviceSoftwareVersion = 00
 * @param line1Number = 16657138888 手机号码
 * @param networkCountryIso = cn
 * @param networkOperator = 46003
 * @param networkOperatorName = 中国电信
 * @param networkType = 6
 * @param phoneType = 2 移动终端类型
 * <ul>
 * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
 * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通</li>
 * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信</li>
 * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
 * </ul>
 * @param simCountryIso = cn
 * @param simOperator = 46003 移动网络运营商名称
 *        <p>中国移动（46000、46002、46007）、如中国联通（46001）、中国电信（46003）</p>
 * @param simOperatorName = 中国电信， sim卡运营商名称
 *        <p>中国移动、如中国联通、中国电信</p>
 * @param simSerialNumber = 89860315045710604022
 * @param simState = 5 sim卡状态
 * @param subscriberId = 460030419724900(IMSI) IMSI码
 * @param voiceMailNumber = *86
 */
@Parcelize
data class PhoneVo(
    val deviceId: String?,
    val deviceSoftwareVersion: String?,
    val line1Number: String?,
    val networkCountryIso: String?,
    val networkOperator: String?,
    val networkOperatorName: String?,
    val networkType: Int,
    val phoneType: Int,
    val simCountryIso: String?,
    val simOperator: String?,
    val simOperatorName: String?,
    val simSerialNumber: String?,
    val simState: Int,
    val subscriberId: String?,
    val voiceMailNumber: String?
) : Parcelable