package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.ActivityFilePreviewBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.base.BaseViewModel
import com.nmk.myapplication.work.ui.fragment.preview.VideoPreviewFragment
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.utils.glide.ImageUtil

/**
 * @desc: 文件详情界面
 * @author：Created by LinManyi on 2024/2/20
 */

class FilePreviewActivity : BaseActivity<BaseViewModel, ActivityFilePreviewBinding>() {

    companion object {
        fun starActivity(context: Context, name: String, path: String) {
            context.startActivity(
                Intent(context, FilePreviewActivity::class.java)
                    .putExtra("name", name)
                    .putExtra("path", path)
            )
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
//        val name = intent.getStringExtra("name") ?: ""
        val path = intent.getStringExtra("path") ?: ""
//        mViewBind.titleBar.setTitle(name)
//        mViewBind.titleBar.visibleOrGone(ImageUtil.isImgLinkerUrl(path))
        mViewBind.titleBar.setLeftImgColor(getColor(R.color.white))
        mViewBind.titleBar.onClickLeftListener = object : TitleBar.OnClickLeftListener {
            override fun leftOnClick(v: View, isBack: Boolean) {
                this@FilePreviewActivity.finish()
            }
        }
        if (ImageUtil.isMp4AnimUrl(path)) {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, VideoPreviewFragment.getInstance(path)).commitAllowingStateLoss()
        }
    }
}