package com.lib.utils.java

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 *
 *
 * @author: Admin.
 * @date  : 2019-08-18.
 */
object DateUtils {

    private const val PATTERN1_FULL =
        "^\\s*\\d{4}(\\-)\\d{1,2}\\1\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\s*$"  //yyyy-MM-dd HH:mm:ss;
    private const val PATTERN1_LONG =
        "^\\s*\\d{4}(\\-)\\d{1,2}\\1\\d{1,2}\\s+\\d{1,2}:\\d{1,2}\\s*$"  //yyyy-MM-dd HH:mm;
    private const val PATTERN1_SHORT = "^\\s*\\d{4}(\\-)\\d{1,2}\\1\\d{1,2}\\s*$"  //yyyy-MM-dd;
    private const val PATTERN2_FULL =
        "^\\s*\\d{1,2}(\\/)\\d{1,2}\\1\\d{4}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\s*$"  //dd/MM/yyyy  HH:mm:ss;
    private const val PATTERN2_LONG =
        "^\\s*\\d{1,2}(\\/)\\d{1,2}\\1\\d{4}\\s+\\d{1,2}:\\d{1,2}\\s*$"  //dd/MM/yyyy  HH:mm;
    private const val PATTERN2_SHORT = "^\\s*\\d{1,2}(\\/)\\d{1,2}\\1\\d{4}\\s*$"  //dd/MM/yyyy;

    private val DATE_PATTERNS = object : LinkedHashMap<String, ThreadLocal<DateFormat>>(8, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, ThreadLocal<DateFormat>>?): Boolean {
            return size > 10
        }
    }

    /**
     * 把日期格式化成短日期格式，只显示小时和分钟。
     *
     *
     * 格式：HH:mm
     *
     *
     * @param date 要格式化的日期.
     * @return String 格式化结果.
     */
    fun formatAsHourAndMinuteOnly(date: Date?): String? {
        if (date == null) {
            return null
        }
        var dateFormat = DATE_PATTERNS["HH:mm"]
        if (dateFormat == null) {
            dateFormat = initDatePatterns("HH:mm")
        }
        return dateFormat.get()!!.format(date)
    }

    /**
     * 将分钟数的时间转成日期类型格式字符串..
     *
     * 格式为：HH:mm
     * <pre>`
     * formatAsTimeByMinutes(60) == "01:00"
     * formatAsTimeByMinutes(61) == "01:01"
    `</pre> *
     * @param minutes 分钟数的时间.
     * @return 时间字符串.
     */
    fun formatAsTimeByMinutes(minutes: Int): String {
        return String.format(Locale.getDefault(), "%02d", minutes / 60) + ":" + String.format(
            Locale.getDefault(),
            "%02d",
            minutes % 60
        )
    }

    /**
     * 将秒钟数的时间转成日期类型格式字符串.
     *
     * 格式为：HH:mm:ss
     * <pre>`
     * formatAsTimeByMinutes(3600) == "01:00:00"
     * formatAsTimeByMinutes(7200) == "10:00:00"
    `</pre> *
     * @param seconds 分钟数的时间.
     * @return 时间字符串.
     */
    fun formatAsTimeBySeconds(seconds: Int): String {
        return String.format(Locale.getDefault(), "%02d", seconds / 3600) + ":" +
                String.format(Locale.getDefault(), "%02d", seconds % 3600 / 60) + ":" +
                String.format(Locale.getDefault(), "%02d", seconds % 3600 % 60)
    }

    /**
     * 把日期格式化成字符串.
     * @param date 日期.
     * @param regex 格式，如 "yyyy-MM-dd","dd/MM/yyyy".
     * @return String 日期字符串.
     */
    fun format(date: Date, regex: String): String {
        var dateFormat = DATE_PATTERNS[regex]
        if (dateFormat == null) {
            dateFormat = initDatePatterns(regex)
        }
        return dateFormat.get()!!.format(date)
    }

    @Synchronized
    private fun initDatePatterns(regex: String): ThreadLocal<DateFormat> {
        var dateFormat = DATE_PATTERNS[regex]
        if (dateFormat == null) {
            dateFormat = object : ThreadLocal<DateFormat>() {
                override fun initialValue(): DateFormat? {
                    return SimpleDateFormat(regex, Locale.getDefault())
                }
            }
            DATE_PATTERNS[regex] = dateFormat
        }
        return dateFormat
    }

    /**
     * 把日期格式字符串解析成日期.
     *
     *
     * 支持的日期包括：
     * "yyyy-MM-dd HH:mm:ss",
     * "yyyy-MM-dd HH:mm",
     * "yyyy-MM-dd"，
     * "dd/MM/yyyy HH:mm:ss",
     * "dd/MM/yyyy HH:mm",
     * "dd/MM/yyyy"
     * @param dateStr 日期字符串.
     * @return Date 日期.
     */
    fun parse(dateStr: String?): Date? {
        if (dateStr.isNullOrBlank()) {
            return null
        }
        var pattern: String? = null
        if (dateStr.contains("-")) {
            if (dateStr.matches(PATTERN1_FULL.toRegex())) {
                pattern = "yyyy-MM-dd HH:mm:ss"
            } else if (dateStr.matches(PATTERN1_LONG.toRegex())) {
                pattern = "yyyy-MM-dd HH:mm"
            } else if (dateStr.matches(PATTERN1_SHORT.toRegex())) {
                pattern = "yyyy-MM-dd"
            }
        } else {
            if (dateStr.matches(PATTERN2_FULL.toRegex())) {
                pattern = "dd/MM/yyyy HH:mm:ss"
            } else if (dateStr.matches(PATTERN2_LONG.toRegex())) {
                pattern = "dd/MM/yyyy HH:mm"
            } else if (dateStr.matches(PATTERN2_SHORT.toRegex())) {
                pattern = "dd/MM/yyyy"
            }
        }
        if (pattern == null) return null
        var dateFormat = DATE_PATTERNS[pattern]
        if (dateFormat == null) {
            dateFormat = initDatePatterns(pattern)
        }
        try {
            return dateFormat.get()!!.parse(dateStr)
        } catch (e: ParseException) {
            throw IllegalArgumentException("Illegal data string:$dateStr", e)
        }

    }

    /**
     * 把日期格式字符串解析成日期.
     * @param dateStr 日期字符串.
     * @param pattern 格式，如 "yyyy-MM-dd","dd/MM/yyyy".
     * @return Date 日期.
     */
    fun parse(dateStr: String, pattern: String): Date? {
        if (dateStr.isNullOrBlank() || pattern.isNullOrBlank()) {
            return null
        }
        var dateFormat = DATE_PATTERNS[pattern]
        if (dateFormat == null) {
            dateFormat = initDatePatterns(pattern)
        }
        try {
            return dateFormat.get()!!.parse(dateStr)
        } catch (e: ParseException) {
            throw IllegalArgumentException("Illegal data string:$dateStr", e)
        }

    }

    /**
     * 根据指定日期和指定的时间字符串解析时间,即把给定日期的时间部分换成指定的时间串.
     * @param sourceDate 源日期.
     * @param shortTime 短时间字符串,格式为"HH:mm:ss"或者"HH:mm"，但不能是"mm:ss"。有效字符串:"08:00:00","08:00".
     * @return Date 返回结果.
     */
    fun parseByTimeString(sourceDate: Date?, shortTime: String): Date {
        if (sourceDate == null) {
            throw IllegalArgumentException("currentData == null")
        }
        val regex = "[0-9]{1,2}:[0-9]{1,2}(:[0-9]{1,2})?"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(shortTime)
        if (matcher.find()) {
            val timeStr = matcher.group()
            val timeArr = timeStr.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val hour = Integer.parseInt(timeArr[0])
            val minute = Integer.parseInt(timeArr[1])
            val second = if (timeArr.size == 3) Integer.parseInt(timeArr[2]) else 0
            val calendar = Calendar.getInstance()
            calendar.time = sourceDate
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, second)
            return calendar.time
        }
        return sourceDate
    }

    /**
     * 得到一天的最早时间.
     *
     * @param date
     * 指定日期.
     * @return 一天的最早时间.
     */
    fun getDateAtZeroTime(date: Date?): Date? {
        if (date == null) {
            return null
        }

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    /**
     * 得到一天的最后时刻.
     *
     * @param date
     * 指定日期.
     * @return 一天的最后时刻.
     */
    fun getDateAtEndTime(date: Date?): Date? {
        if (date == null) {
            return null
        }

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        return calendar.time

    }

    /**
     * 获取当月月初此时的时间。
     * 如果要获取零点时间可使用 [.getDateAtZeroTime].
     * 如果要获取结束时间可使用 [.getDateAtEndTime].
     * @return [Date] 当月第一天此时时间
     */
    fun getDateAtBeginOfMonth(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DATE, 1)// 设为当前月的1号
        return calendar.time
    }

    /**
     * 获取当月月末此时的日期。
     * 如果要获取零点时间可使用 [.getDateAtZeroTime].
     * 如果要获取结束时间可使用 [.getDateAtEndTime].
     * @return [Date] 当月最后一天此时时间。
     */
    fun getDateAtEndOfMonth(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.DATE, -1)

        return cal.time
    }

    /**
     * 将微秒数的时间转成日期类型.
     * @param timeMillis 微秒数的时间.
     * @return 日期.
     */
    fun getDateByTimeMillis(timeMillis: Long?): Date? {
        if (timeMillis == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.timeInMillis = timeMillis
        return c.time
    }

    /**
     * 将日期转化成距离当天零点的分钟数.
     * @param date 日期
     * @return 距离零点的分钟数
     */
    fun getMinutesFromZeroTime(date: Date?): Int {
        if (date == null) return 0
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        return hours * 60 + minutes
    }

    /**
     * 将日期字符串转化成距离当天零点的分钟数.
     * @param timeStr 日期时间字符串.格式必须包含"HH:mm",可以是"08:00","08:00:00","10-01 08:00:00"等
     * @return 距离零点的分钟数
     */
    fun getMinutesFromZeroTime(timeStr: String?): Int {
        if (timeStr == null) {
            return 0
        }
        val regex = "[0-9]{1,2}:[0-9]{1,2}"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(timeStr)
        if (matcher.find()) {
            val group = matcher.group()
            val times = group.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (times.size != 2) {
                return 0
            }
            val hours = Integer.valueOf(times[0])
            val minutes = Integer.valueOf(times[1])
            return hours * 60 + minutes
        }
        return 0
    }

    /**
     * 将日期转化成距离当天零点的秒数.
     * @param date 日期
     * @return 距离零点的秒数
     */
    fun getSecondsFromZeroTime(date: Date?): Int {
        if (date == null) return 0
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val seconds = calendar.get(Calendar.SECOND)
        return hours * 3600 + minutes * 60 + seconds
    }

    /**
     * 将日期字符串转化成距离当天零点的秒数.
     * @param timeStr 日期时间字符串.格式必须包含"HH:mm:ss",可以是"08:00:00","10-01 08:00:00"等
     * @return 距离零点的秒数
     */
    fun getSecondsFromZeroTime(timeStr: String?): Int {
        if (timeStr == null) {
            return 0
        }
        val regex = "[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(timeStr)
        if (matcher.find()) {
            val group = matcher.group()
            val times = group.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (times.size != 3) {
                return 0
            }
            val hours = Integer.valueOf(times[0])
            val minutes = Integer.valueOf(times[1])
            val seconds = Integer.valueOf(times[2])
            return hours * 3600 + minutes * 60 + seconds
        }
        return 0
    }

    /**
     * 获取某个日期零点时的时间戳，yyyy-MM-dd 格式的时间戳。
     * @param date 源日期
     * @return 时间戳，参数为null时，返回当前时间戳
     */
    fun getTimeMillisAtZeroTime(date: Date): Long {
        val zeroDate = getDateAtZeroTime(date)
        return zeroDate?.time ?: System.currentTimeMillis()
    }

    /**
     * 获取某时间段前后小时的时间
     * @param date 源日期
     * @param offset 改变的小时个数
     * @return Date 改变后的时间
     */
    fun getDateByHourOffset(date: Date?, offset: Int): Date? {
        if (date == null)
            return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + offset)
        return calendar.time
    }

    /**
     * 获取某时间段前后分钟数的时间
     * @param date 源日期
     * @param offset 改变的分钟数
     * @return Date 改变后的时间
     */
    fun getDateByMinuteOffset(date: Date?, offset: Int): Date? {
        if (date == null)
            return null
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + offset)
        return calendar.time
    }

    /**
     * 得到当天时间偏移指定天数的日期
     * @param offset 偏移的天数
     * @return Date 改变后的时间
     */
    fun getDateByDayOffset(offset: Int): Date {
        val calendar = Calendar.getInstance()// 此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, offset) // 正数为未来某天，负数则为过去某天
        return calendar.time
    }

    /**
     * 得到指定时间偏移指定天数的日期
     * @param date 源日期
     * @param offset 偏移的天数
     * @return Date 改变后的时间
     */
    fun getDateByDayOffset(date: Date, offset: Int): Date {
        val calendar = Calendar.getInstance()// 此时打印它获取的是系统当前时间
        calendar.time = date
        calendar.add(Calendar.DATE, offset) // 正数为未来某天，负数则为过去某天
        return calendar.time
    }

    /**
     * 得到当天时间的下一天
     */
    fun getNextDay(): Date {
        val calendar = Calendar.getInstance()// 此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, +1) // 得到下一天
        return calendar.time
    }

    /**
     * 获得本周一的日期
     * @return [Date]
     */
    fun getMondayOfWeek(): Date {
        val mondayPlus = getMondayPlus()
        val currentDate = GregorianCalendar()
        currentDate.add(GregorianCalendar.DATE, mondayPlus)
        return currentDate.time

    }

    /**
     * 获得本周日的日期.
     * @return [Date]
     */
    fun getSundayOfWeek(): Date {
        val mondayPlus = getMondayPlus()
        val currentDate = GregorianCalendar()
        currentDate.add(GregorianCalendar.DATE, mondayPlus)
        currentDate.add(GregorianCalendar.DATE, 6)
        return currentDate.time

    }

    // 获得当前日期与本周日相差的天数
    private fun getMondayPlus(): Int {
        val cd = Calendar.getInstance()
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        val dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1 // 因为按中国礼拜一作为第一天所以这里减1
        return if (dayOfWeek == 1) {
            0
        } else {
            1 - dayOfWeek
        }
    }

    /**
     * 获取指定日期为该月第几天
     * @param date 日期
     * @return String 当月的第几天，如 1,2,3，。。。
     */
    fun getDayNumber(date: Date?): String {
        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return day.toString()
    }

    /**
     * 根据日期获得星期
     * @param date  日期
     * @return String 根据星期返回 "1"."2","3"...
     */
    fun getWeekNumber(date: Date): String {
        // String[] weekDaysName = {"星期日" , "星期一", "星期二", "星期三", "星期四", "星期五","星期六"};
        val weekDaysCode = arrayOf("1", "2", "3", "4", "5", "6", "7")
        val calendar = Calendar.getInstance()
        calendar.time = date
        val intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        return weekDaysCode[intWeek]
    }
}