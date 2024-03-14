package com.nmk.myapplication.work.ui.dialog

import android.content.Context
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.PositionPopupView
import com.lxj.xpopup.enums.DragOrientation
import com.lxj.xpopup.enums.PopupAnimation
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.MainMenuDialogBinding
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.utils.view.ViewUtil

/**
 * @desc: 左侧菜单
 * @author：Created by my on 2024/3/14
 */

class MainMenuDialog(context: Context): PositionPopupView(context) {

    private lateinit var mViewBinding: MainMenuDialogBinding

    override fun getImplLayoutId() = R.layout.main_menu_dialog

        companion object {
        fun showDialog(context: Context) {
            MainMenuDialog(context).apply {
                XPopup.Builder(context)
                    .hasShadowBg(true)
                    .popupAnimation(PopupAnimation.TranslateFromLeft)
                    .popupHeight(ViewUtil.getScreenHeight())
                    .enableDrag(true)
                    .asCustom(this).show()
            }
        }
    }

    override fun getDragOrientation(): DragOrientation {
        return DragOrientation.DragToLeft
    }

    override fun onCreate() {
        super.onCreate()
        mViewBinding = MainMenuDialogBinding.bind(popupImplView)
        mViewBinding.pictureTv.setClickNotDoubleListener {

        }
    }
}