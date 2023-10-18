package com.nmk.myapplication.utils.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间工具类
 *
 * @author Created by H-ray on 2022/6/16.
 */
object CommonDateFormatUtil {

    /** 年月日 时分秒 **/
    private const val formatHMSYMD = "yyyy/MM/dd HH:mm:ss"

    /** 年月日 时分 **/
    private const val formatHMYMD = "yyyy/MM/d HH:mm"

    /** 年月日 **/
    private const val formatYMD = "yyyy/MM/dd"

    /** 年月 **/
    private const val formatYM = "yyyy/MM"

    /**年**/
    private const val formatY = "yyyy"

    /** 月日 时分 **/
    private const val formatHMMD = "MM/dd HH:mm"

    /** 月日 时分秒 **/
    private const val formatHMSMD = "MM/dd HH:mm:ss"

    /**  月日 **/
    private const val formatMD = "MM/dd"

    /**  时分 **/
    private const val formatHM = "HH:mm"


    fun getFormatHMSYMD(currentTime: Long) = getDateFormat(currentTime, formatHMSYMD)

    fun getFormatHMYMD(currentTime: Long) = getDateFormat(currentTime, formatHMYMD)

    fun getFormatHMSYM(currentTime: Long) = getDateFormat(currentTime, formatHMSMD)

    fun getFormatHMMD(currentTime: Long) = getDateFormat(currentTime, formatHMMD)

    fun getFormatYMD(currentTime: Long) = getDateFormat(currentTime, formatYMD)

    fun getFormatYM(currentTime: Long) = getDateFormat(currentTime, formatYM)

    fun getFormatY(currentTime: Long) = getDateFormat(currentTime, formatY)

    fun getFormatMD(currentTime: Long) = getDateFormat(currentTime, formatMD)

    fun getFormatHM(currentTime: Long) = getDateFormat(currentTime, formatHM)

    fun getStampHMSYMD(dateFormat: String) = dateToStamp(dateFormat, formatHMSYMD)

    fun getStampHMYMD(dateFormat: String) = dateToStamp(dateFormat, formatHMYMD)

    fun getStampHMSYM(dateFormat: String) = dateToStamp(dateFormat, formatHMSMD)

    fun getStampHMMD(dateFormat: String) = dateToStamp(dateFormat, formatHMMD)

    fun getStampYMD(dateFormat: String) = dateToStamp(dateFormat, formatYMD)

    fun getStampYM(dateFormat: String) = dateToStamp(dateFormat, formatYM)

    fun getStampY(dateFormat: String) = dateToStamp(dateFormat, formatY)

    fun getStampMD(dateFormat: String) = dateToStamp(dateFormat, formatMD)

    fun getStampHM(dateFormat: String) = dateToStamp(dateFormat, formatHM)

    private fun getDateFormat(currentTime: Long, format: String): String {
        return try {
            SimpleDateFormat(format, Locale.ENGLISH).format(Date(currentTime))
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 转时间戳
     */
    private fun dateToStamp(s: String, dateFormat: String): String? {
        val res: String
        //设置时间模版
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val date = simpleDateFormat.parse(s)
        val ts = date.time
        res = ts.toString()
        return res
    }

    /**
     * 获取聊天时间格式化
     * formatTime : 需要格式化的时间
     * targetTime : 对比时间
     */
    fun getChatTimeFormat(formatTime: Long, targetTime: Long): String {
        if (formatTime <= 0) return ""
        return when (dataDiff(formatTime, targetTime)) {
            1 -> {
                //同一天，时分
                getFormatHM(formatTime)
            }
            2, 3 -> {
                //同一年，月日时分
                getFormatHMMD(formatTime)
            }
            else -> {
                //不同年，年月日
                getFormatYMD(formatTime)
            }
        }
    }

    /**
     * 判断两个时间戳的差异
     * 是否同一天: @return 1
     * 是否同一月: @return 2
     * 是否同一年: @return 3
     * 其他      : @return 4
     */
    fun dataDiff(mTime: Long, tTime: Long): Int {
        val cCal = Calendar.getInstance()
        cCal.time = Date(mTime)
        val mYear = cCal[Calendar.YEAR]
        val mMonth = cCal[Calendar.MONTH] + 1
        val mDate = cCal[Calendar.DATE]

        val nowCal = Calendar.getInstance()
        nowCal.time = Date(tTime)
        val tYear = nowCal[Calendar.YEAR]
        val tMonth = nowCal[Calendar.MONTH] + 1
        val tDate = nowCal[Calendar.DATE]

        return if (mYear == tYear && mMonth == tMonth && mDate == tDate) {
            //同一天
            1
        } else if (mYear == tYear && mMonth == tMonth) {
            //同一月
            2
        } else if (mYear == tYear) {
            //同一年
            3
        } else {
            //不同年
            4
        }
    }

    /**
     * 获取时间时分秒
     * time : 毫秒数
     * needHours : 是否需要显示小时
     */
    fun getHMSFormat(time: Long, needHours: Boolean = true): String {
        var hours = (time / 1000 / 3600).toInt()
        var minutes = (time / 1000 / 60 % 60).toInt()
        var seconds = (time / 1000 % 60).toInt()

        if (time <= 0) {
            //处理容错
            hours = 0
            minutes = 0
            seconds = 0
        }

        return if (needHours) {
            "${getIntFormat(hours)}:${getIntFormat(minutes)}:${getIntFormat(seconds)}"
        } else {
            "${getIntFormat(minutes)}:${getIntFormat(seconds)}"
        }
    }

    /**
     * 获取时间分秒，不足一分钟显示0，
     * time : 毫秒数
     * needHours : 是否需要显示小时
     */
    fun getMediaTime(time: Long): String {
        var minutes = (time / 1000 / 60 % 60).toInt()
        var seconds = (time / 1000 % 60).toInt()

        if (time <= 0) {
            //处理容错
            minutes = 0
            seconds = 0
        }

        return "$minutes:${getIntFormat(seconds)}"
    }

    /**
     * 获取化整时间(传入单位：秒)
     */
    fun getIntegrateTime(time: Int): String {
        val days = (time / 60 / 60 / 24)
        if (days != 0) {
            return "${days}Day"
        }

        val hours = (time / 60 / 60 % 24)
        if (hours != 0) {
            return "${hours}Hours"
        }

        val minutes = (time / 60 % 60)
        if (minutes != 0) {
            return "${minutes}Min"
        }

        val seconds = (time % 60)
        return "${seconds}s"
    }

    /**
     * 不足补0操作
     */
    private fun getIntFormat(num: Int) = if (num >= 10) num.toString() else "0$num"

    /**
     * 获取当前时区，部分8.0手机会报错：AssertionError
     */
    private var timeZoneCache: String? = null
    fun getTimeZone(): String {
        try {
            if (timeZoneCache?.isNotEmpty() == true) {
                return timeZoneCache ?: ""
            }
            timeZoneCache = Calendar.getInstance().timeZone.getDisplayName(false, TimeZone.SHORT)
        } catch (e: AssertionError) {
            e.printStackTrace()
        }
        return timeZoneCache ?: ""
    }

    /**
     * 获取当前沙特时间距离今晚凌晨的剩余时间（毫秒数）
     * @return - (一天时间 - 沙特当前时间(时/分/秒))
     */
    fun getMorningCountdown(): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"))
        val currTimeSecond = calendar[Calendar.HOUR_OF_DAY] * 3600 + calendar[Calendar.MINUTE] * 60 + calendar[Calendar.SECOND]
        return (86400 - currTimeSecond) * 1000L
    }

    /**
     * 获取时间时分秒
     * time : 毫秒数
     */
    fun getDHOrHMS(time: Long): String {
        var day = (time / 1000 / 3600 / 24).toInt()
        var hours = (time / 1000 / 3600 % 24).toInt()
        var minutes = (time / 1000 / 60 % 60).toInt()
        var seconds = (time / 1000 % 60).toInt()

        if (time <= 0) {
            //处理容错
            day = 0
            hours = 0
            minutes = 0
            seconds = 0
        }

        return if (day > 0) {
            if (hours > 0) {
                "${day}Day${getIntFormat(hours)}H"
            } else {
                "${day}Day"
            }
        } else if (hours > 0) {
            val numberFormat = DecimalFormat("#,##0.0", DecimalFormatSymbols(Locale.ENGLISH))
            val formattedNumber = numberFormat.format((time / 1000 / 3600f))
            "${formattedNumber}h"
        } else {
            "${getIntFormat(hours)}:${getIntFormat(minutes)}:${getIntFormat(seconds)}"
        }
    }

}