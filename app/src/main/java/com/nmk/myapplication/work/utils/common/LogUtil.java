package com.nmk.myapplication.work.utils.common;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.databinding.ktx.BuildConfig;

import com.nmk.myapplication.app.MyApplication;
import com.nmk.myapplication.work.utils.log.XLogDiskManager;

/**
 * Log 工具类
 * <p>
 * Created by H-ray on 2018/1/18
 */
public class LogUtil {

    private static final String TAG = "sysTag";
    private static final String TAG_IM = "imTag";
    private static final String TAG_LIVE = "liveTag";
    public static final String TAG_HTTP = "httpTag";
    private static final String TAG_PUSH = "pushTag";
    private static final String TAG_ERROR = "errorTag";
    public static final String TAG_DEBUG = "debugTag";
    public static final String VALUE_FORMAT = "╔════════<%1s>════════\n║%2s\n╚═════════════════════";

    public static void i(String info) {
        i(TAG, info);
    }

    public static void i(String tag, String info) {
        iNotToFile(tag, info);
        writeToXLog(tag, info, tag.equals(TAG_HTTP) || tag.equals(TAG_ERROR));
    }

    public static void iNotToFile(String tag, String info) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, info);
        }
    }

    public static void http(String httpStr) {
        i(TAG_HTTP, httpStr);
    }

    /**
     * 打印IM聊天模块
     */
    public static void chat(String info) {
        i(TAG_IM, info);
    }

    /**
     * 打印直播日志
     */
    public static void live(String info) {
        i(TAG_LIVE, info);
    }

    /**
     * 打印推送日志
     */
    public static void push(String info) {
        i(TAG_PUSH, info);
    }

    /**
     * 打印崩溃日志
     */
    public static void collapse(String error) {
        String infoFormat = String.format(VALUE_FORMAT, TAG_ERROR, error);
        i(TAG_ERROR, infoFormat);
    }

    /**
     * 打印点击事件【一些重要的点击事件】
     */
    public static void click(View v) {
        String value = "";
        if (v != null) {
            try {
                String idValue = MyApplication.mContext.getResources().getResourceEntryName(v.getId());
                if (v instanceof TextView) {
                    value = v.getClass().getName() + "(" + idValue + ")" + "(" + ((TextView) v).getText() + ")";
                } else {
                    value = v.getClass().getName() + "(" + idValue + ")";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(value)) {
            i("onClickTag", value);
        }

    }

    public static void writeToXLog(String tag, String logStr, boolean formatStyle) {
        XLogDiskManager.Companion.getInstance().writeSys(
                String.format(formatStyle ? "[%1s]<%2s>：\n%3s" : "[%1s]<%2s>：%3s",
                        CommonDateFormatUtil.INSTANCE.getFormatHMSYM(System.currentTimeMillis()),
                        tag,
                        logStr)
        );
    }

}
