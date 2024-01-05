package com.nmk.myapplication.work.ui.view.fileView

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.nmk.myapplication.databinding.FileViewPreviewBinding
import com.nmk.myapplication.work.utils.glide.ImageUtil
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.visible

class FilePreviewView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mViewBinder: FileViewPreviewBinding
    init {
        mViewBinder = FileViewPreviewBinding.inflate(LayoutInflater.from(context),this)
    }

    fun setData(url: String) {
        if (ImageUtil.isImgLinkerUrl(url)) {
            mViewBinder.imv.visible()
            mViewBinder.videoV.gone()
            ImageUtil.loadImg(context, mViewBinder.imv, url)
        } else {
            mViewBinder.imv.gone()
            mViewBinder.videoV.visible()
            initVideoBg(url)
        }
    }

    private fun initVideoBg(url: String) {
        val uri = url
        mViewBinder.videoV.setVideoPath(Uri.parse(uri).toString())
//        mViewBinder.videoV.requestFocus()
        mViewBinder.videoV.setOnCompletionListener { mViewBinder.videoV.start() }
    }

    override fun onDetachedFromWindow() {
        mViewBinder.videoV.setOnCompletionListener(null)
        super.onDetachedFromWindow()
    }
}