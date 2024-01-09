package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.luck.picture.lib.utils.ToastUtils
import com.nmk.myapplication.databinding.ActivitySecondBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.common.loading.LoadingManager
import com.nmk.myapplication.work.ui.dialog.FileMoreDialog
import com.nmk.myapplication.work.ui.view.fileView.FilePreviewView
import com.nmk.myapplication.work.ui.view.titlebar.TitleBar
import com.nmk.myapplication.work.vm.FileMV
import java.io.File

/**
 * 预览文件
 */
class SecondActivity : BaseActivity<FileMV, ActivitySecondBinding>() {

    private val previewViews: MutableList<FilePreviewView> = arrayListOf() //显示图片的ImageView
    private var position = 0 //从上个页面获取的子项position
    private var files: ArrayList<FileInfo> = arrayListOf() //上个页面获取的URL列表

    companion object {
        fun startActivity(context: Context, fileInfos: ArrayList<FileInfo>, position: Int) {
            val intent = Intent(context, SecondActivity::class.java)
            intent.putParcelableArrayListExtra("list", fileInfos)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    private fun initData(list: ArrayList<FileInfo>) {
        files = list
        for (i in files.indices)  //获取图片，设置PhotoView，加到ViewPager当中
        {
            val photoView = FilePreviewView(this)
            photoView.setData(files[i])
            previewViews.add(photoView)
        }
        mViewBind.vp.adapter = MyViewPagerAdapter()
        mViewBind.vp.setCurrentItem(position, true)
        mViewBind.vpText.text = "${position + 1}/${previewViews.size}"
    }

    override fun initView(bundle: Bundle?) {
        position = intent.getIntExtra("position", 0)
        files = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("list", FileInfo::class.java)?: arrayListOf()
        } else {
            intent.getParcelableArrayListExtra("list")?: arrayListOf()
        }
        initData(files)
        mViewBind.vp.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) { //左右滑动时更新中心点和文字信息
                this@SecondActivity.position = position
                mViewBind.vpText.text = "${position + 1}/${previewViews.size}"
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        initEvent()
        mViewBind.titleBar.onClickLeftListener = object : TitleBar.OnClickLeftListener{
            override fun leftOnClick(v: View, isBack: Boolean) {
                finish()
            }

        }
    }

    private fun initEvent() {
        mViewBind.deleteTv.setClickNotDoubleListener {
            //删除
            mViewModel.deleteFile(this, arrayListOf(files[position]))
        }
        mViewBind.detailTv.setClickNotDoubleListener {
            //详情
            FileMoreDialog.showDialog(this,files[position].id)
        }
        mViewBind.moveTv.setClickNotDoubleListener {
            //移动
        }
        mViewBind.shareTv.setClickNotDoubleListener {
            //分享
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            val file = File(files[position].content)
            val fromFile = Uri.fromFile(file)
            sendIntent.putExtra(Intent.EXTRA_EMAIL, fromFile)
            sendIntent.type = "*/image"
            startActivity(sendIntent)
        }
    }

    override fun createObserver() {
        mViewModel.deleteFileED.observeInActivity(this) {
            LoadingManager.getInstance().hideDialog()
            if (it)
                finish()
            else
                ToastUtils.showToast(this,"删除失败")
        }
    }

    private inner class MyViewPagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return previewViews.size
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view: View = previewViews[position]
            container.addView(view)
            return view
        }
    }

}