package com.nmk.myapplication.work.utils.glide;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.nmk.myapplication.R;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Created by H-ray on 2020/3/29
 * @desc : 图片加载工具
 */
public class ImageUtil {

    /**
     * 加载原图，解决adjustViewBounds变模糊的问题
     * @param context
     * @param imgView
     * @param imgUrl
     */
    public static void loadOriginalImg(Context context, ImageView imgView, String imgUrl) {
        imgUrl = getHttps(imgUrl);
        if (isDestroy(context)) {
            return;
        }
        if (imgUrl != null && imgUrl.endsWith(".gif")) {
            imgUrl = getHttps(imgUrl);
            Glide.with(context).asGif()
                    .load(imgUrl)
                    .diskCacheStrategy(getDiskCacheStrategy(imgUrl))
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(imgView);
        } else {
            imgUrl = getHttps(imgUrl);
            Glide.with(context).load(imgUrl)
                    .diskCacheStrategy(getDiskCacheStrategy(imgUrl))
                    .dontAnimate()
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(imgView);
        }
    }

    /**
     * 加载网络图片
     */
    public static void loadImg(Context context, ImageView imgView, String imgUrl) {
        imgUrl = getHttps(imgUrl);
        if (imgUrl != null && imgUrl.endsWith(".gif")) {
            loadGif(context, imgView, imgUrl);
        } else {
            loadImg(context, imgView, imgUrl, R.drawable.img_img_default);
        }
    }

    /**
     * 加载图片并设置默认暂未图
     */
    public static void loadImg(Context context, ImageView imgView, String imgUrl, int placeholderImgId) {
        if (isDestroy(context)) {
            return;
        }
        imgUrl = getHttps(imgUrl);
        Glide.with(context).load(imgUrl)
                .placeholder(placeholderImgId)
                .error(placeholderImgId)
                .diskCacheStrategy(getDiskCacheStrategy(imgUrl))
                .dontAnimate()
                .into(imgView);
    }

    /**
     * 加载本地图片
     */
    public static void loadImg(Context context, ImageView imgView, int resourceId) {
        if (isDestroy(context)) {
            return;
        }

        Glide.with(context).load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate().into(imgView);
    }

    public static void loadGif(Context context, ImageView imgView, int imgResource) {
        if (isDestroy(context)) {
            return;
        }
        Glide.with(context).asGif().load(imgResource).into(imgView);
    }

    public static void loadGif(Context context, ImageView imgView, String imgUrl) {
        if (isDestroy(context)) {
            return;
        }
        imgUrl = getHttps(imgUrl);
        Glide.with(context).asGif().load(imgUrl).diskCacheStrategy(getDiskCacheStrategy(imgUrl)).into(imgView);
    }

    /**
     * 加载圆形网络图片
     */
    public static void loadCircleImg(Context context, ImageView imgView, String imgUrl) {
        loadCircleImg(context, imgView, imgUrl, R.drawable.img_header_default);
    }

    public static void loadCircleImg(Context context, ImageView imgView, String imgUrl, int defaultResource) {
        if (isDestroy(context)) {
            return;
        }
        imgUrl = getHttps(imgUrl);
        Glide.with(context).load(imgUrl)
                .placeholder(defaultResource)
                .diskCacheStrategy(getDiskCacheStrategy(imgUrl))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .dontAnimate().into(imgView);
    }

    public static void loadCircleImg(Context context, ImageView imgView, int imgResource) {
        if (isDestroy(context)) {
            return;
        }
        Glide.with(context).load(imgResource)
                .placeholder(R.drawable.img_header_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .dontAnimate().into(imgView);
    }

    /**
     * 加载webp动态图片
     */
    public static void loadWebp(Context context, ImageView imgView, int imgResource) {
        if (isDestroy(context)) {
            return;
        }
        Transformation<Bitmap> centerCrop = new CenterCrop();
        Glide.with(context)
                .load(imgResource)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .optionalTransform(centerCrop)
                .into(imgView);
    }

    /**
     * 加载webp动态图片
     */
    public static void loadWebp(Context context, ImageView imgView, String url, String cacheKeyStr) {
        if (isDestroy(context)) {
            return;
        }
        //添加唯一缓存键（这是因为webp动态复用的特殊性，复用同一个webp动态后，就不会动了，因此需要添加webP缓存键）
        ObjectKey cacheKey = new ObjectKey(cacheKeyStr);
        Transformation<Bitmap> centerCrop = new CenterCrop();
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(getDiskCacheStrategy(url))
                .optionalTransform(centerCrop)
                .signature(cacheKey)
                .into(imgView);
    }

    public static void loadRoundImg(Context context, ImageView imgView, String imgUrl, int round) {
        loadRoundImg(context, imgView, imgUrl, round, R.drawable.img_img_default, R.drawable.img_img_default);
    }

    public static void loadRoundImg(Context context, ImageView imgView, String imgUrl, int round, int loadDefaultResource, int errorDefaultResource) {
        loadRoundImg(context, imgView, imgUrl, round, round, round, round, loadDefaultResource, errorDefaultResource);
    }

    /**
     * 加载圆角网络图片(可设置四个不同圆角)
     *
     * @param ltRound 设置圆角
     */
    public static void loadRoundImg(Context context, ImageView imgView, String imgUrl, int ltRound, int lbRound, int rtRound, int rbRound, int loadDefaultResource, int errorDefaultResource) {
        if (isDestroy(context)) {
            return;
        }
        imgUrl = getHttps(imgUrl);
        GranularRoundedCorners brc;
//        if (RTLUtils.INSTANCE.isRTL()) {
//            //如果是RTL布局，需要左右互换
//            brc = new GranularRoundedCorners((float) rtRound, (float) ltRound, (float) lbRound, (float) rbRound);
//        } else {
            brc = new GranularRoundedCorners((float) ltRound, (float) rtRound, (float) rbRound, (float) lbRound);
//        }

        if (imgUrl != null && imgUrl.endsWith(".gif")) {
            Glide.with(context).asGif().load(imgUrl)
                    .placeholder(loadDefaultResource)
                    .error(errorDefaultResource)
                    .diskCacheStrategy(getDiskCacheStrategy(imgUrl))
                    .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new CenterCrop(), brc)))
                    .into(imgView);
        } else {
            Glide.with(context).load(imgUrl)
                    .skipMemoryCache(false)
                    .placeholder(loadDefaultResource)
                    .error(errorDefaultResource)
                    .diskCacheStrategy(getDiskCacheStrategy(imgUrl))
                    .apply(RequestOptions.bitmapTransform(new MultiTransformation<>(new CenterCrop(), brc)))
                    .dontAnimate().into(imgView);
        }
    }

    private static DiskCacheStrategy getDiskCacheStrategy(String imgUrl) {
        if (isWebpImg(imgUrl)) {
            return DiskCacheStrategy.DATA;
        } else {
            return DiskCacheStrategy.ALL;
        }
    }

    public static String getHttps(String imgUrl) {
        if (imgUrl != null && imgUrl.startsWith("http") && !imgUrl.startsWith("https")) {
            imgUrl = "https" + imgUrl.substring(4);
        }
        return imgUrl;
    }

    public static Boolean isImgLinkerUrl(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return false;
        }
        String end = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
        end = end.toLowerCase();
        return !TextUtils.isEmpty(end) && (end.equals("png") || end.equals("jpg") || end.equals("mpeg") || end.equals("jpeg") || end.equals("webp") || end.equals(
                "gif"));
    }

    public static Boolean isWebpImg(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return false;
        }
        String end = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
        end = end.toLowerCase();
        return end.endsWith("webp");
    }

    public static Boolean isMp4AnimUrl(String url) {
        return !TextUtils.isEmpty(url) && (url.endsWith(".mp4.zip") || url.endsWith(".mp4"));
    }

    public static Boolean isPagAnimUrl(String url) {
        return !TextUtils.isEmpty(url) && (url.endsWith(".pag.zip") || url.endsWith(".pag"));
    }

    public static void loadBlurImg(Context context, ImageView imgView, String imgUrl, int blurRadius) {
        if (isDestroy(context)) {
            return;
        }
        Glide.with(context).asBitmap().load(imgUrl).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                Blurry.with(context)
//                        .radius(blurRadius)
//                        .sampling(8)
//                        .from(resource).into(imgView);
                return true;
            }
        }).preload();
    }

    /**
     * 加载文件图片
     * @param context
     * @param imageView
     */
    public static void loadFile(Context context, ImageView imageView, String filePath) {
        Glide.with(context)
                .load(new File(filePath)) // filePath为图片文件的路径
//                .apply(options)
                .into(imageView);
    }

    /**
     * 加载网络.9背景图
     */
    public static void loadNinePatchBackground(Context context, View frameLayout, String imgUrl, int loadDefaultResource) {
        if (isDestroy(context)) {
            return;
        }
        Glide.with(context)
                .asFile()
                .load(getHttps(imgUrl))
                .skipMemoryCache(false)
                .into(new CustomTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull @NotNull File resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super File> transition) {
                        Bitmap bitmap = BitmapFactory.decodeFile(resource.getPath());
                        if (bitmap == null) {
                            frameLayout.setBackgroundResource(loadDefaultResource);
                            return;
                        }
                        byte[] chunk = bitmap.getNinePatchChunk();
//                        if (NinePatch.isNinePatchChunk(chunk)) {
//                            NinePatchDrawable npd = new NinePatchDrawable(
//                                    context.getResources(),
//                                    bitmap, chunk, NinePatchChunk.deserialize(chunk).mPaddings, null);
//                            frameLayout.setBackground(npd);
//                        } else {
//                            frameLayout.setBackground(new BitmapDrawable(context.getResources(), bitmap));
//                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 清除Glide的图片加载请求
     */
    public static void clearImage(Context context, ImageView imgView) {
        if (!isDestroy(context)) {
            Glide.with(context).clear(imgView);
        }
    }

    /**
     * 上下文是否为空，activity是否关闭
     */
    public static boolean isDestroy(Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            return isDestroy((Activity) context);
        }
        return false;
    }

    /**
     * 判断context是否已经被销毁
     */
    //判断Activity是否Destroy
    public static boolean isDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

}
