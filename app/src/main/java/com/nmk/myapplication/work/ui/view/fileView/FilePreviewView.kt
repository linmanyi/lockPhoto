package com.nmk.myapplication.work.ui.view.fileView

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.FileViewPreviewBinding
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.activity.FilePreviewActivity
import com.nmk.myapplication.work.utils.glide.ImageUtil
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.visible

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
            FilePreviewActivity.starActivity(context, info.fileName, info.content)
        }
    }
}