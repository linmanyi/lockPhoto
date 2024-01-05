package com.nmk.myapplication.work.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.nmk.myapplication.databinding.ActivitySecondBinding
import com.nmk.myapplication.work.base.BaseActivity
import com.nmk.myapplication.work.date.FileInfo
import com.nmk.myapplication.work.ext.setClickNotDoubleListener
import com.nmk.myapplication.work.ui.view.fileView.FilePreviewView
import com.nmk.myapplication.work.vm.FileMV

/**
 * 预览文件
 */
class SecondActivity : BaseActivity<FileMV, ActivitySecondBinding>() {

    private val previewViews: MutableList<FilePreviewView> = arrayListOf() //显示图片的ImageView
    private var position = 0 //从上个页面获取的子项position
    private var urls: List<String> = ArrayList() //上个页面获取的URL列表
    private val adapter = MyViewPagerAdapter()

    companion object {
        fun startActivity(context: Context, fileInfos: List<FileInfo>, position: Int) {
            val intent = Intent(context, SecondActivity::class.java)
            //intent.putStringArrayListExtra("urls", (ArrayList<String>) images);
            val images = ArrayList<String>()
            for ((_, _, _, content) in fileInfos) {
                images.add(content)
            }
            intent.putStringArrayListExtra("urls", images)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    private fun initData() {
        urls = intent.getStringArrayListExtra("urls") ?: arrayListOf()
        for (i in urls.indices)  //获取图片，设置PhotoView，加到ViewPager当中
        {
            val photoView = FilePreviewView(this)
            photoView.setData(urls[i])
            previewViews.add(photoView)
        }
        mViewBind.vp.adapter = adapter
        mViewBind.vp.setCurrentItem(position - 1, true)
        mViewBind.vpText.text = "$position/${previewViews.size}"
    }

    override fun initView(bundle: Bundle?) {
        position = intent.getIntExtra("position", 0)
        initData()
        mViewBind.vp.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) { //左右滑动时更新中心点和文字信息
                this@SecondActivity.position = position
                mViewBind.vpText.text = "${position + 1}/${previewViews.size}"
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        initEvent()
    }

    private fun initEvent() {
        mViewBind.deleteTv.setClickNotDoubleListener {
            //删除
        }
        mViewBind.detailTv.setClickNotDoubleListener {
            //详情
//            FileMoreDialog.showDialog(this,)
        }
        mViewBind.moveTv.setClickNotDoubleListener {
            //移动
        }
        mViewBind.shareTv.setClickNotDoubleListener {
            //分享
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
//            val file = File()
//            val fromFile = Uri.fromFile(file)
//            sendIntent.putExtra(Intent.EXTRA_EMAIL, fromFile)
            sendIntent.type = "*/image"
            startActivity(sendIntent)
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