package com.nmk.myapplication.work.ui.view.fileView

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.ImageViewerPopupView
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener
import com.lxj.xpopup.util.SmartGlideImageLoader
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileViewPreviewBinding
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.isDestroy
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.activity.FilePreviewActivity
import com.nmk.myapplication.work.utils.glide.ImageUtil

class FilePreviewView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mViewBinder: FileViewPreviewBinding

    init {
        mViewBinder = FileViewPreviewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setData(info: FileInfo) {
        if (ImageUtil.isImgLinkerUrl(info.content)) {
            ImageUtil.loadImg(context, mViewBinder.imv, info.content)
        } else {
            ImageUtil.loadImg(context, mViewBinder.imv, R.mipmap.icon_mp4)
        }
        mViewBinder.imv.setClickNotDoubleListener {
            if (ImageUtil.isImgLinkerUrl(info.fileName)) {
                showBigImage(context,mViewBinder.imv, listOf(info.content),0)
            } else {
                FilePreviewActivity.starActivity(context, info.fileName, info.content)
            }
        }
    }

    private fun showBigImage(context: Context, imageView: ImageView, list: List<String>, position: Int) {
        if (context.isDestroy()) return

        XPopup.Builder(context).asImageViewer(imageView, position, list,
            { popupView, position -> //这里更新一下弹出的图片，因为滑动后原来的图片就改变了，回缩动画需要根据当前显示的图片来进行
                val selImgView = mViewBinder.imv
                popupView.updateSrcView(selImgView)
            }, SmartGlideImageLoader())
            .isShowPlaceholder(false)
            .isShowSaveButton(false)
            .show()
    }
}