package com.nmk.myapplication.work.utils.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;

import com.luck.picture.lib.immersive.RomUtils;
import com.nmk.myapplication.app.MyApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * View工具类
 * <p>
 * Created by H-ray on 2018/1/18
 */
public class ViewUtil {
    /**
     * 设置状态栏透明
     */
    public static void TranslucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.transparent));
            //window.setNavigationBarColor(android.R.color.transparent);
        }
    }

    /**
     * 动态设置全屏与非全屏
     */
    public static void isFull(Activity activity, boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attr);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 根据手机分辨率从DP转成PX
     */
    public static int dip2px(float dpValue) {
        return dip2px(MyApplication.Companion.getMContext(), dpValue);
    }

    public static int dip2px(Context context, float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        final float fontScale =
                MyApplication.Companion.getMContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     */
    public static int px2dip(float pxValue) {
        return px2dip(MyApplication.Companion.getMContext(), pxValue);
    }

    public static int px2dip(Context context,float pxValue) {
        float scale =context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(float pxValue) {
        final float fontScale =
                MyApplication.Companion.getMContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);

    }

    /**
     * 判断输入框是否为空
     */
    public static boolean etIsEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }

    /**
     * 获取输入框的值
     */
    public static String getEtText(EditText et) {
        return et.getText().toString().trim();
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        return MyApplication.Companion.getMContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return MyApplication.Companion.getMContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕高度2，先简单处理一下适配问题
     * getScreenHeight（），部分手机拿到的高度包含状态栏，部分手机不包含状态栏，这里与100求余，简单判断
     *
     * @return
     */
    public static int getRealScreenHeight() {
        int height = getScreenHeight();
        if (height % 100 == 0 || height == 1920 || height == 1280) {
            return height;
        }
        return height + getStatusBarHeight();
    }

    /**
     * 获取导航栏高度
     */
    public static int getNavigationBarHeight() {
        if (checkDeviceHasNavigationBar()) {
            Resources resources = MyApplication.Companion.getMContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        }

        return 0;
    }

    /**
     * Return whether the navigation bar visible.
     * <p>Call it in onWindowFocusChanged will get right result.</p>
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNavBarVisible(@NonNull final Activity activity) {
        return isNavBarVisible(activity.getWindow());
    }

    /**
     * Return whether the navigation bar visible.
     * <p>Call it in onWindowFocusChanged will get right result.</p>
     *
     * @param window The window.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNavBarVisible(@NonNull final Window window) {
        boolean isVisible = false;
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = getResNameById(id);
                if ("navigationBarBackground".equals(resourceEntryName)
                        && child.getVisibility() == View.VISIBLE) {
                    isVisible = true;
                    break;
                }
            }
        }
        if (isVisible) {
            // 对于三星手机，android10以下非OneUI2的版本，比如 s8，note8 等设备上，
            // 导航栏显示存在bug："当用户隐藏导航栏时显示输入法的时候导航栏会跟随显示"，会导致隐藏输入法之后判断错误
            // 这个问题在 OneUI 2 & android 10 版本已修复
            if (RomUtils.isSamsung()
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                try {
                    return Settings.Global.getInt(MyApplication.Companion.getMContext().getContentResolver(), "navigationbar_hide_bar_enabled") == 0;
                } catch (Exception ignore) {
                }
            }

            int visibility = decorView.getSystemUiVisibility();
            isVisible = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        }

        return isVisible;
    }

    private static String getResNameById(int id) {
        try {
            return MyApplication.Companion.getMContext().getResources().getResourceEntryName(id);
        } catch (Exception ignore) {
            return "";
        }
    }


    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = MyApplication.Companion.getMContext().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    /**
     * 获取屏幕亮度
     */
    public static float getScreenBrightness(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        return lp.screenBrightness;
    }

    /**
     * 设置屏幕亮度
     *
     * @param value : 0-1之间浮点（-1可以恢复屏幕亮度）
     */
    public static void setScreenBrightness(Activity activity, float value) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = value;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 获取系统音量
     */
    public static int[] getCurrVoice(Activity activity) {
        AudioManager mAudioManager = (AudioManager) activity.getSystemService(
                Context.AUDIO_SERVICE);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return new int[]{max, current};
    }

    /**
     * 设置系统音量
     *
     * @param curr : 最大音量百分比
     */
    public static void setCurrVoice(Activity activity, float curr) {
        AudioManager mAudioManager = (AudioManager) activity.getSystemService(
                Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (max * curr), 0);
    }

    /**
     * 防止头条适配导致获取不正确的状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 通过反射修改 MenuItem item颜色
     */
    public static void setActionBarText(Activity activity, int colorResource) {
        try {
            final LayoutInflater inflater = activity.getLayoutInflater();
            @SuppressLint("SoonBlockedPrivateApi") Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater, false);
            LayoutInflaterCompat.setFactory2(inflater, new LayoutInflater.Factory2() {
                @Nullable
                @Override
                public View onCreateView(@Nullable View parent, @NonNull String name,
                                         @NonNull Context context, @NonNull AttributeSet attrs) {
                    if (name.equalsIgnoreCase("android.support.v7.view.menu.IconMenuItemView")
                            || name.equalsIgnoreCase(
                            "android.support.v7.view.menu.ActionMenuItemView")) {
                        final View view;
                        try {
                            view = inflater.createView(name, null, attrs);
                            if (view instanceof TextView) {
                                ((TextView) view).setTextColor(
                                        activity.getResources().getColor(colorResource));
                            }
                            return view;
                        } catch (ClassNotFoundException | InflateException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Nullable
                @Override
                public View onCreateView(@NonNull String name, @NonNull Context context,
                                         @NonNull AttributeSet attrs) {
                    return null;
                }
            });
        } catch (Exception e) {

        }
    }

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object objGet;
        for (String i : arr) {
            try {
                f = imm.getClass().getDeclaredField(i);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                objGet = f.get(imm);
                if (objGet != null && objGet instanceof View) {
                    View vGet = (View) objGet;
                    if (vGet.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 将View置灰（代码实现将一个View变成灰色，通常用于某些不可用View的状态）
     *
     * @param view
     * @param saturation: (0:置灰，1恢复原始)
     */
    public static void setGray(View view, float saturation) {
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(saturation);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        view.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    /**
     * 获取View的 Bitmap
     *
     * @param v view
     * @return Bitmap
     */
    public static Bitmap getBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        int viewW = v.getWidth();
        if(viewW <= 0){
            return null;
        }
        Bitmap screenshot = Bitmap.createBitmap(viewW, v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        v.draw(c);
        return screenshot;
    }

}
