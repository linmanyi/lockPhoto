package com.nmk.myapplication.work.helper.picture

import android.content.Context
import androidx.core.content.ContextCompat
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.InjectResourceSource
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnInjectLayoutResourceListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.nmk.myapplication.R
import com.nmk.myapplication.app.MyApplication
import com.nmk.myapplication.work.ui.common.loading.LoadingManager


object PictureSelectHelper {

    val selectorStyle by lazy { PictureSelectorStyle() }

    fun getFiles(context: Context, select: ArrayList<LocalMedia?>?, result1: ((ArrayList<LocalMedia?>?) -> Unit)? = null) {
        initSelector()
        PictureSelector.create(context)
            .openGallery(SelectMimeType.ofAll())
            .setSelectorUIStyle(selectorStyle)
            .isDisplayCamera(true)
            .setSelectedData(select)
            .isGif(true)
            .isFastSlidingSelect(true)
            .setMaxSelectNum(99)
            .setMaxVideoSelectNum(99)
            .setImageEngine(GlideEngine.createGlideEngine())
            //todo:压缩
//            .setCompressEngine(ImageCompressEngine())
            //todo:语言切换
//            .setLanguage(LanguageConfig.AR)
            .setInjectLayoutResourceListener(MeOnInjectLayoutResourceListener())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    result1?.invoke(result)
                }

                override fun onCancel() {
                    LoadingManager.getInstance().hideDialog()
                }
            })
    }

    private fun initSelector(){
        val whiteTitleBarStyle = TitleBarStyle()
        val whiteBottomNavBarStyle = BottomNavBarStyle()
        val selectMainStyle = SelectMainStyle()

        whiteTitleBarStyle.titleBackgroundColor = ContextCompat.getColor(MyApplication.mContext,R.color.white)
        whiteTitleBarStyle.titleLeftBackResource = R.mipmap.ic_x_back
        whiteTitleBarStyle.titleDrawableRightResource = R.mipmap.icon_select_arrow_down
        whiteTitleBarStyle.isHideCancelButton = true
        whiteTitleBarStyle.titleTextColor = ContextCompat.getColor(MyApplication.mContext,R.color.textColor_333333)

        whiteBottomNavBarStyle.bottomNarBarBackgroundColor = ContextCompat.getColor(MyApplication.mContext,R.color.white)
        whiteBottomNavBarStyle.bottomSelectNumResources = R.drawable.bg_picture_bottom_select
        whiteBottomNavBarStyle.bottomSelectNumTextColor = ContextCompat.getColor(MyApplication.mContext,R.color.white)
        whiteBottomNavBarStyle.bottomSelectNumTextSize = 14
        whiteBottomNavBarStyle.bottomPreviewSelectTextColor = ContextCompat.getColor(MyApplication.mContext,R.color.white)
        whiteBottomNavBarStyle.bottomPreviewNormalTextColor = ContextCompat.getColor(MyApplication.mContext,R.color.main_color)

        selectMainStyle.isDarkStatusBarBlack = true
        selectMainStyle.mainListBackgroundColor = ContextCompat.getColor(MyApplication.mContext,R.color.white)
        selectMainStyle.selectNormalTextSize = 14
        selectMainStyle.selectNormalTextColor = ContextCompat.getColor(MyApplication.mContext,R.color.white)
        selectMainStyle.selectTextColor = ContextCompat.getColor(MyApplication.mContext,R.color.white)
        selectMainStyle.selectTextSize = 14

        selectorStyle.titleBarStyle = whiteTitleBarStyle
        selectorStyle.bottomBarStyle = whiteBottomNavBarStyle
        selectorStyle.selectMainStyle = selectMainStyle
    }

    /**
     * 注入自定义布局UI，前提是布局View id 和 根目录Layout必须一致
     */
    private class MeOnInjectLayoutResourceListener : OnInjectLayoutResourceListener {
        override fun getLayoutResourceId(context: Context, resourceSource: Int): Int {
            return when (resourceSource) {
                InjectResourceSource.MAIN_SELECTOR_LAYOUT_RESOURCE -> R.layout.layout_picture_selector
                InjectResourceSource.PREVIEW_LAYOUT_RESOURCE -> R.layout.layout_picture_preview
//                InjectResourceSource.MAIN_ITEM_IMAGE_LAYOUT_RESOURCE -> R.layout.ps_custom_item_grid_image
//                InjectResourceSource.MAIN_ITEM_VIDEO_LAYOUT_RESOURCE -> R.layout.ps_custom_item_grid_video
//                InjectResourceSource.MAIN_ITEM_AUDIO_LAYOUT_RESOURCE -> R.layout.ps_custom_item_grid_audio
//                InjectResourceSource.ALBUM_ITEM_LAYOUT_RESOURCE -> R.layout.ps_custom_album_folder_item
//                InjectResourceSource.PREVIEW_ITEM_IMAGE_LAYOUT_RESOURCE -> R.layout.ps_custom_preview_image
//                InjectResourceSource.PREVIEW_ITEM_VIDEO_LAYOUT_RESOURCE -> R.layout.ps_custom_preview_video
//                InjectResourceSource.PREVIEW_GALLERY_ITEM_LAYOUT_RESOURCE -> R.layout.ps_custom_preview_gallery_item
                else -> 0
            }
        }
    }
}