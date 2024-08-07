package com.nmk.myapplication.work.ui.fragment.preview

import android.os.Bundle
import cn.jzvd.Jzvd
import com.nmk.myapplication.databinding.FragmentVideoPreviewBinding
import com.nmk.myapplication.work.base.BaseFragment
import com.nmk.myapplication.work.base.BaseViewModel


/**
 * @desc: 音视频详情
 * @author：Created by My on 2024/2/20
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
        mViewBind.videoView.setUp(
            uri,
            ""
        )
//        mViewBind.videoView.posterImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos();
    }

    override fun lazyLoadData() {

    }
}