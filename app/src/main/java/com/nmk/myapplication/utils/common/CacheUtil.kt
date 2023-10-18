package com.nmk.myapplication.utils.common


import com.tencent.mmkv.MMKV

/**
 * 缓存工具
 *
 * @author Created by H-ray on 2022/12/29.
 */
object CacheUtil {

    /**
     * 缓存时间标记
     */
    private const val timeCacheKeyFlag = "_CacheTime"

    private val mmkv: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    /**
     * 添加String类型缓存
     */
    fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    /**
     * 获取String类型缓存
     */
    fun getString(key: String, default: String = ""): String {
        return mmkv.decodeString(key, default) ?: ""
    }

    /**
     * 添加String类型缓存
     */
    fun putStringByCacheTime(key: String, value: String) {
        mmkv.encode("$key$timeCacheKeyFlag", System.currentTimeMillis())
        mmkv.encode(key, value)
    }

    /**
     * 获取String类型缓存
     */
    fun getStringByCacheTime(key: String, default: String = "", cacheTime: Long): String? {
        val lastTime = getLong(getCacheTimeKey(key))
        if (System.currentTimeMillis() - lastTime <= cacheTime) {
            return getString(key, default)
        } else {
            return null
        }
    }

    /**
     * 添加Int类型缓存
     */
    fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    /**
     * 获取Int类型缓存
     */
    fun getInt(key: String, default: Int = 0): Int {
        return mmkv.decodeInt(key, default)
    }

    /**
     * 添加Boolean类型缓存
     */
    fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    /**
     * 添加Boolean类型缓存,添加本地缓存时间
     */
    fun putBooleanByCacheTime(key: String, value: Boolean) {
        mmkv.encode("$key$timeCacheKeyFlag", System.currentTimeMillis())
        mmkv.encode(key, value)
    }

    /**
     * 获取Boolean类型缓存
     */
    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return mmkv.decodeBool(key, default)
    }

    /**
     * 获取Boolean类型缓存，且在缓存有效期内
     */
    fun getBooleanCacheTime(key: String, default: Boolean = false, cacheTime: Long): Boolean? {
        val lastTime = getLong(getCacheTimeKey(key))
        if (System.currentTimeMillis() - lastTime <= cacheTime) {
            return getBoolean(key, default)
        } else {
            return null
        }
    }

    /**
     * 添加Long类型缓存
     */
    fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    /**
     * 获取Long类型缓存
     */
    fun getFloat(key: String, default: Float = 0f): Float {
        return mmkv.decodeFloat(key, default)
    }

    /**
     * 添加Long类型缓存
     */
    fun putLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    /**
     * 添加Long类型添加本地缓存时间
     */
    fun putLongByCacheTime(key: String, value: Long) {
        mmkv.encode("$key$timeCacheKeyFlag", System.currentTimeMillis())
        mmkv.encode(key, value)
    }

    /**
     * 获取Long类型缓存
     */
    fun getLong(key: String, default: Long = 0): Long {
        return mmkv.decodeLong(key, default)
    }

    /**
     * 获取Long类型缓存，且在缓存有效期内
     */
    fun getLongCacheTime(key: String, default: Long = 0, cacheTime: Long): Long {
        val lastTime = getLong(getCacheTimeKey(key))
        if (System.currentTimeMillis() - lastTime <= cacheTime) {
            return getLong(key, default)
        } else {
            return default
        }
    }

    private fun getCacheTimeKey(key: String) = "$key$timeCacheKeyFlag"

    /**
     * 添加对象类型缓存
     */
    fun <T> putMode(key: String, value: T?) {
        if (value == null) {
            mmkv.encode(key, "")
        } else {
            mmkv.encode(key, GsonInstance.getInstance().toJson(value))
        }
    }

    /**
     * 添加对象类型缓存，添加本地缓存有效期
     */
    fun <T> putModeByCacheTime(key: String, value: T?) {
        mmkv.encode("$key$timeCacheKeyFlag", System.currentTimeMillis())
        if (value == null) {
            mmkv.encode(key, "")
        } else {
            mmkv.encode(key, GsonInstance.getInstance().toJson(value))
        }
    }

    /**
     * 获取对象类型缓存
     */
    fun <T> getMode(key: String, m: Class<T>): T? {
        val modeString = mmkv.decodeString(key)
        if (modeString.isNullOrBlank()) {
            return null
        } else {
            kotlin.runCatching {
                return GsonInstance.getInstance().fromJson(modeString, m)
            }
            return null
        }
    }

    /**
     * 获取对象类型缓存
     */
    fun <T> getModeByCacheTime(key: String, m: Class<T>, cacheTime: Long): T? {
        val lastTime = getLong(getCacheTimeKey(key))
        if (System.currentTimeMillis() - lastTime <= cacheTime) {
            val modeString = mmkv.decodeString(key)
            if (modeString.isNullOrBlank()) {
                return null
            } else {
                kotlin.runCatching {
                    return GsonInstance.getInstance().fromJson(modeString, m)
                }
                return null
            }
        } else {
            return null
        }
    }

    /**
     * 获取今天某个操作的次数
     * 把次数和今天的时间戳年月日结合一下
     */
    fun getCurrDayFrequency(key: String): Int {
        val keyResult = getString(key, "")
        kotlin.runCatching {
            val dataFormat = keyResult.substring(0, keyResult.lastIndexOf("-"))
            if(dataFormat != CommonDateFormatUtil.getFormatYMD(System.currentTimeMillis())){
                //不是同一天，返回0
                return 0
            }else{
                return keyResult.substring(keyResult.lastIndexOf("-") + 1).toInt()
            }
        }
        return 0
    }

    /**
     * 设置次数，根据当前天数
     * 把次数和今天的时间戳年月日结合一下
     */
    fun setCurrDayFrequency(key: String, frequency: Int) {
        putString(key, "${CommonDateFormatUtil.getFormatYMD(System.currentTimeMillis())}-$frequency")
    }

    /**
     * 清楚某个key
     */
    fun removeKey(key: String) {
        mmkv.remove(key)
    }

}