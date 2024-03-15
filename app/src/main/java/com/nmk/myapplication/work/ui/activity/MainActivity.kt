package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemSwipe
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.R
import com.nmk.myapplication.databinding.ActivityMainBinding
import com.nmk.myapplication.databinding.FolderItemBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FolderInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.dialog.AddFolderDialog
import com.nmk.myapplication.work.ui.dialog.MainMenuDialog
import com.nmk.myapplication.work.ui.dialog.TipsDialog
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.utils.common.CommonDateFormatUtil.getFormatHMYMD
import com.nmk.myapplication.work.utils.glide.ImageUtil
import com.nmk.myapplication.work.vm.MainVM
import me.hgj.jetpackmvvm.ext.view.visibleOrGone

/**
 * 主页
 */
class MainActivity : BaseActivity<MainVM, ActivityMainBinding>() {

    private lateinit var adapter: BindingAdapter

    companion object{
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        adapter = mViewBind.content.setup {
            addType<FolderInfo> { R.layout.folder_item }
            onBind {
                val binding = getBinding<FolderItemBinding>()
                val contentView = binding.contentView
                val model = getModel<FolderInfo>()
                if (model.cover.isNotEmpty()) {
                    ImageUtil.loadFile(this@MainActivity,contentView.coverImg,model.cover)
                }
                contentView.titleTv.text = model.fileName
                contentView.dateTv.text = getFormatHMYMD(model.createTime).replace("/","-")
                binding.contentView.rootView.setClickNotDoubleListener {
                    //进入文件夹
                    FolderDetailActivity.startActivity(this@MainActivity,model.id,model.fileName)
                }
                binding.deleteImv.setClickNotDoubleListener {
                    TipsDialog.Builder()
                        .setTitle(getString(R.string.tip))
                        .setTips(getString(R.string.delete_photo_album_content))
                        .addBtn(getString(R.string.cancel))
                        .addBtn(getString(R.string.sure), getColor(R.color.main_color))
                        .create(this@MainActivity)
                        .setOnBtnClickListener { it1 ->
                            if (it1 == 1) {
                                //确认清除
                                mViewModel.deleteFolder(model.id,model.fileName)
                            }
                        }
                }
            }
//            onClick(R.id.moreImv) {
//                //更多
//                val model = getModel<FolderInfo>()
//                MainEditActivity.startActivity(this@MainActivity,model.id)
//            }
        }
        mViewBind.titleBar.onClickRightListener = object : TitleBar.OnClickRightListener{
            override fun rightOnClick(v: View, position: Int) {
                AddFolderDialog.showDialog(this@MainActivity) {
                    mViewModel.addFolder(this@MainActivity,it)
                }
            }
        }
        mViewBind.settingImv.setClickNotDoubleListener {
            MainMenuDialog.showDialog(this)
//            SettingActivity.startActivity(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getData()
    }

    override fun createObserver() {
        mViewModel.getDataED.observeInActivity(this) {
            mViewBind.content.models = it
            mViewBind.emptyTv.visibleOrGone(it.isEmpty())
        }
        mViewModel.deleteFolderED.observeInActivity(this) {
            if (it) {
                mViewModel.getData()
            } else {
                ToastUtils.showToast(this,getString(R.string.delete_failure))
            }
        }
        mViewModel.addEd.observeInActivity(this) {
            ToastUtils.showToast(this,it)
        }
    }
}