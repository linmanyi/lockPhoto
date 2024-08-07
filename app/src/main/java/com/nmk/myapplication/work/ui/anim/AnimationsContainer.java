package com.nmk.myapplication.work.ui.anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

/**
 * TITLE
 * Created by shixiaoming on 16/12/27.
 */

public class AnimationsContainer {
    public int FPS = 58;  // 每秒播放帧数，fps = 1/t，t-动画两帧时间间隔
    private int resId = 0; //图片资源
    // 单例
    private static AnimationsContainer mInstance;


    private AnimationsContainer(){}

    //获取单例
    public static AnimationsContainer getInstance(int resId, int fps) {
        if (mInstance == null)
            mInstance = new AnimationsContainer();
        mInstance.setResId(resId, fps);
        return mInstance;
    }

    public void setResId(int resId, int fps){
        this.resId = resId;
        this.FPS = fps;
    }

    /**
     * @return progress dialog animation
     */
    public FramesSequenceAnimation createProgressDialogAnim(Context context, ImageView imageView) {
        return new FramesSequenceAnimation(imageView, getData(context, resId), FPS);
    }


    /**
     * 循环读取帧---循环播放帧
     */
    public static class FramesSequenceAnimation {
        private final int[] mFrames; // 帧数组
        private int mIndex; // 当前帧
        private boolean mShouldRun; // 开始/停止播放用
        private boolean mIsRunning; // 动画是否正在播放，防止重复播放
        private final SoftReference<ImageView> mSoftReferenceImageView; // 软引用ImageView，以便及时释放掉
        private final Handler mHandler;
        private final int mDelayMillis;
        //private BitmapFactory.Options mBitmapOptions;//Bitmap管理类，可有效减少Bitmap的OOM问题

        public FramesSequenceAnimation(ImageView imageView, int[] frames, int fps) {
            mHandler = new Handler();
            mFrames = frames;
            mIndex = -1;
            mSoftReferenceImageView = new SoftReference(imageView);
            mShouldRun = false;
            mIsRunning = false;
            mDelayMillis = 1000 / fps;//帧动画时间间隔，毫秒
            imageView.setImageResource(mFrames[0]);
        }
        //循环读取下一帧
        private int getNext() {
            mIndex++;
            if (mIndex >= mFrames.length)
                mIndex = 0;
            return mFrames[mIndex];
        }

        /**
         * 播放动画，同步锁防止多线程读帧时，数据安全问题
         */
        public synchronized void start() {
            mShouldRun = true;
            if (mIsRunning)
                return;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = mSoftReferenceImageView.get();
                    if (!mShouldRun || imageView == null) {
                        mIsRunning = false;
                        return;
                    }

                    mIsRunning = true;
                    //新开线程去读下一帧
                    mHandler.postDelayed(this, mDelayMillis);

                    if (imageView.isShown()) {
                        imageView.setImageResource(getNext());
                    }

                }
            };

            mHandler.post(runnable);
        }

        /**
         * 停止播放
         */
        public synchronized void stop() {
            mShouldRun = false;
        }

    }

    /**
     * 从xml中读取帧数组
     */
    private int[] getData(Context mContext, int resId){
        TypedArray array = mContext.getResources().obtainTypedArray(resId);

        int len = array.length();
        int[] intArray = new int[array.length()];

        for(int i = 0; i < len; i++){
            intArray[i] = array.getResourceId(i, 0);
        }
        array.recycle();
        return intArray;
    }

}