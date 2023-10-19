package com.nmk.myapplication.work.manager

import android.app.Activity
import com.nmk.myapplication.work.manager.ActivityManager.HolderInstance.mStack
import java.util.*

/**
 *
 * @author Created by H-ray on 2020/7/4
 *
 * @desc : 自定义任务栈管理器
 *
 */
class ActivityManager private constructor() {

    companion object {
        @JvmStatic
        fun getInstance() = HolderInstance.instance
    }

    object HolderInstance {
        var instance = ActivityManager()
        var mStack = Stack<Activity>()
    }

    /**
     * 添加Activity
     */
    fun pushActivity(activity: Activity?) {
        mStack.add(activity)
    }

    /**
     * 移除某个Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            mStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        //从栈顶开始便利，移除全部的Activity
        val tempList = mutableListOf<Activity>()
        for (i in mStack.indices.reversed()) {
            if (mStack[i].javaClass == cls) {
                tempList.add(mStack[i])
            }
        }
        for (item in tempList) {
            finishActivity(item)
        }
        tempList.clear()
    }

    /**
     * 弹出类名的activity（返回键或者滑动返回调用）
     */
    @Synchronized
    fun pupActivity(activity: Activity) {
        if (mStack.isEmpty()) return
        for (i in mStack.indices.reversed()) {
            if (mStack[i] === activity) {
                //弹出该Activity
                mStack.remove(mStack[i])
                break
            }
        }
    }

    /**
     * finish所有的activity，保留栈底activity
     */
    @Synchronized
    fun finishAllActivityExMain() {
        while (mStack.size > 1) {
            val activity = mStack.pop()
            finishActivity(activity)
        }
    }

    /**
     * finish所有的activity
     */
    @Synchronized
    fun finishAllActivity() {
        while (mStack.size > 0) {
            val activity = mStack.pop()
            finishActivity(activity)
        }
    }

    /**
     * 获取栈顶Activity
     */
    fun getTopActivity(): Activity? {
        var topActivity: Activity? = null
        for (i in mStack.indices.reversed()) {
            topActivity = mStack.peek()
            if (topActivity != null && !topActivity.isFinishing && !topActivity.isDestroyed) {
                return topActivity
            }
        }
        return topActivity
    }

    /**
     * 根据Class获取Activity
     */
    fun getActivityForClass(cls: Class<*>): Activity? {
        for (activity in mStack) {
            if (activity.javaClass == cls) {
                return activity
            }
        }
        return null
    }

    /**
     * 是否存在该Activity
     */
    fun isExistActivity(cls: Class<*>): Boolean {
        for (activity in mStack) {
            if (activity.javaClass == cls) {
                return true
            }
        }
        return false
    }

    /**
     * 移除除cls以外的全部Activity
     *
     * @param cls
     */
    @Synchronized
    fun finishAllExclude(cls: Class<*>) {
        val iterator = mStack.iterator()
        while (iterator.hasNext()) {
            val a = iterator.next()
            if (a.javaClass != cls) {
                iterator.remove()
                a.finish()
            }
        }
    }

    /**
     * 关闭当前cls以上的新入栈的Activity
     */
    @Synchronized
    fun finishExcludeTagetActivityUp(cls: Class<*>) {
        if (!isExistActivity(cls)) return
        for (i in mStack.indices.reversed()) {
            if (mStack[i].javaClass === cls) {
                return
            } else {
                finishActivity(mStack[i])
            }
        }
    }

    /**
     * 当前页是否从栈中移除
     *
     * @param activity
     * @param classes
     * @return
     */
    private fun isEmpty(activity: Activity, classes: Array<Class<*>>): Boolean {
        val cls: Class<*> = activity.javaClass
        for (cl in classes) {
            if (cls == cl) return true
        }
        return false
    }

}