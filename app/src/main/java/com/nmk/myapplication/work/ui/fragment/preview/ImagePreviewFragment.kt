package com.nmk.myapplication.work.ui.fragment.preview

import android.graphics.BitmapFactory
import android.os.Bundle
import com.nmk.myapplication.databinding.FragmentImagePreviewBinding
import com.nmk.myapplication.work.base.BaseFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.util.get

/**
 * @desc: 图片详情
 * @author：Created by LinManyi on 2024/2/20
 */

class ImagePreviewFragment: BaseFragment<BaseViewModel, FragmentImagePreviewBinding>() {
    companion object {
        fun getInstance(path: String): ImagePreviewFragment {
            return ImagePreviewFragment().apply {
                arguments = Bundle().apply {
                    putString("path", path)
                }
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val path = arguments?.getString("path", "")
        mViewBind.contentImv.setMaxZoom(4f)
        mViewBind.contentImv.setImageBitmap(BitmapFactory.decodeFile(path))
    }

    override fun lazyLoadData() {

    }
}