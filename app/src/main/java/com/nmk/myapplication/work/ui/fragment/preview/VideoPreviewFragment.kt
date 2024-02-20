package com.nmk.myapplication.work.ui.fragment.preview

import android.net.Uri
import android.os.Bundle
import com.nmk.myapplication.databinding.FragmentVideoPreviewBinding
import com.nmk.myapplication.work.base.BaseFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.util.get

/**
 * @desc: 音视频详情
 * @author：Created by LinManyi on 2024/2/20
 */

class VideoPreviewFragment : BaseFragment<BaseViewModel, FragmentVideoPreviewBinding>() {

    companion object {
        fun getInstance(path: String): VideoPreviewFragment {
            return VideoPreviewFragment().apply {
                arguments = Bundle().apply {
                    putString("path", path)
                }
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val path = arguments?.getString("path", "")
        initVideoBg(path ?: "")
    }

    private fun initVideoBg(url: String) {
        val uri = url
        mViewBind.videoV.setVideoPath(Uri.parse(uri).toString())
//        mViewBinder.videoV.requestFocus()
        mViewBind.videoV.setOnCompletionListener { mViewBind.videoV.start() }
    }

    override fun onDestroy() {
        mViewBind.videoV.setOnCompletionListener(null)
        super.onDestroy()
    }

    override fun lazyLoadData() {

    }
}