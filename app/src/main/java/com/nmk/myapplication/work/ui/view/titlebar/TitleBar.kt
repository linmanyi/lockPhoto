package com.nmk.myapplication.work.ui.view.titlebar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.nmk.myapplication.R
import com.nmk.myapplication.work.ext.px
import com.nmk.myapplication.work.ext.setClickNotDoubleListener

/**
 *
 * @author Created by H-ray on 2020/7/1
 *
 * @desc : 自定义标题栏
 *
 */
class TitleBar : FrameLayout {

    private lateinit var leftImg: ImageView
    private lateinit var titleTv: TextView
    private lateinit var thisRootView: View
    private val enableBack: Boolean
    private lateinit var rightLinearLayout: LinearLayout
    var onClickLeftListener: OnClickLeftListener? = null
    var onClickRightListener: OnClickRightListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("ResourceAsColor")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(context)
        val typeArrays = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)

        setTitle(
            typeArrays.getString(R.styleable.TitleBar_title),
            typeArrays.getColor(R.styleable.TitleBar_title_color, Color.parseColor("#222324"))
        )

        enableBack = typeArrays.getBoolean(R.styleable.TitleBar_enable_back, true)
        val leftSrcResource =
            typeArrays.getResourceId(R.styleable.TitleBar_left_src, R.mipmap.icon_back)
        if (leftSrcResource == R.mipmap.icon_back && !enableBack) {
            leftImg.visibility = View.GONE
        } else {
            setLeftSrc(leftSrcResource)
        }

        addRightText(
            typeArrays.getString(R.styleable.TitleBar_right_text),
            typeArrays.getColor(
                R.styleable.TitleBar_right_text_color,
                ContextCompat.getColor(context, R.color.white)
            ),
            typeArrays.getDimension(R.styleable.TitleBar_right_text_size, 14f),
            if (typeArrays.getBoolean(
                    R.styleable.TitleBar_right_bold,
                    false
                )
            ) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        )
        addRightSrc(typeArrays.getResourceId(R.styleable.TitleBar_right_src, 0))
    }

    private fun addView(context: Context) {
        View.inflate(context, R.layout.h_widget_titlebar, this)
        leftImg = findViewById(R.id.leftImg)
        leftImg.setClickNotDoubleListener {
            onClickLeftListener?.leftOnClick(leftImg, enableBack)
        }
        titleTv = findViewById(R.id.titleTv)
        rightLinearLayout = findViewById(R.id.rightLinearLayout)
        thisRootView = findViewById(R.id.rootView)
    }

    fun setTitleBarFitsSystemWindows(fitsSystemWindows: Boolean) {
        thisRootView.fitsSystemWindows = fitsSystemWindows
    }

    /**
     * 设置title显示在左边
     * @param startMargin Float  左边距
     */
    fun setTitleShowLeft(startMargin: Float = 16f) {
        ConstraintSet().apply {
            clone(titleTv.parent as ConstraintLayout)
            clear(R.id.titleTv, ConstraintSet.END)
            connect(
                R.id.titleTv,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            connect(
                R.id.titleTv,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                startMargin.toInt().px()
            )
            applyTo(titleTv.parent as ConstraintLayout)
        }
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        titleTv.text = title
    }

    fun setTitle(title: String?, titleColor: Int) {
        titleTv.text = title
        titleTv.setTextColor(titleColor)
    }

    fun getTitleView() = titleTv

    /**
     * 设置左侧图标
     */
    fun setLeftSrc(resourceId: Int) {
        leftImg.setImageResource(resourceId)
    }

    fun enableLeftImg(isShow: Boolean) {
        leftImg.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * 设置右侧文字
     */
    fun addRightText(
        text: String?,
        textColor: Int,
        textSize: Float,
        typeface: Typeface = Typeface.DEFAULT
    ) {
        if (text == null) {
            return
        }
        val viewPadding = if (isInEditMode) 8 else 8.px()
        val height = if (isInEditMode) 44 else 44.px()
        val rightTv = TextView(context)
        rightTv.text = text
        rightTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        rightTv.setTextColor(textColor)
        rightTv.gravity = Gravity.CENTER
        rightTv.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height)
        rightTv.setPaddingRelative(0, 0, viewPadding, 0)
        rightTv.typeface = typeface
        addRightView(rightTv)
    }

    /**
     * 设置右侧图标
     * @param position 大于等于0的情况下按位置添加
     */
    fun addRightSrc(resourceId: Int, position: Int = -1, padding: Float = 8f) {
        if (resourceId > 0) {
            val rightImg = ImageView(context)
            rightImg.setImageResource(resourceId)
            addRightView(rightImg, position, padding)
        }
    }

    /**
     * 设置右侧图标
     */
    fun addRightSrc(
        resourceId: Int,
        position: Int = -1,
        startPadding: Float = 0f,
        endPadding: Float = 0f,
        topPadding: Float = 0f,
        bottomPadding: Float = 0f
    ) {
        if (resourceId > 0) {
            val rightImg = ImageView(context)
            rightImg.setImageResource(resourceId)
            addRightView(rightImg, position, startPadding, endPadding, topPadding, bottomPadding)
        }
    }

    /**
     * 设置右侧View
     * @param position 大于等于0的情况下按位置添加
     */
    private fun addRightView(view: View, position: Int = -1, padding: Float = 8f) {
        addRightView(view, position, 0f, padding, padding, padding)
    }

    /**
     * 设置右侧View
     */
    private fun addRightView(
        view: View,
        position: Int = -1,
        startPadding: Float,
        endPadding: Float,
        topPadding: Float,
        bottomPadding: Float
    ) {

        rightLinearLayout.removeView(view)
        val height = if (!isInEditMode) 44.px() else 44
        view.layoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height)

        val viewStartPadding =
            if (!isInEditMode) startPadding.toInt().px() else startPadding.toInt()
        val viewEndPadding = if (!isInEditMode) endPadding.toInt().px() else endPadding.toInt()
        val viewTopPadding = if (!isInEditMode) topPadding.toInt().px() else topPadding.toInt()
        val viewBottomPadding =
            if (!isInEditMode) bottomPadding.toInt().px() else bottomPadding.toInt()

        view.setPaddingRelative(viewStartPadding, viewTopPadding, viewEndPadding, viewBottomPadding)
        if (position >= 0) {
            rightLinearLayout.addView(view, position)
        } else {
            rightLinearLayout.addView(view)
        }
        view.setClickNotDoubleListener {
            onClickRightListener?.rightOnClick(view, rightLinearLayout.indexOfChild(view))
        }

    }


    /**
     * 移除右侧全部View
     */
    fun removeAllRightView() {
        rightLinearLayout.removeAllViews()
    }

    fun replaceRightView(old_view: View, view: View) {
        rightLinearLayout.removeView(old_view)
        addRightView(view)
    }

    fun removeRightViewAt(index: Int) {
        rightLinearLayout.removeViewAt(index)
    }

    fun setRightTextColor(color: Int) {
        for (index in 0..rightLinearLayout.childCount) {
            val text = rightLinearLayout.getChildAt(index)
            if (text is TextView) {
                text.setTextColor(color)
            }
        }
    }

    /**
     * 设置右侧图片颜色
     */
    fun setRightImgColor(color: Int) {
        for (index in 0..rightLinearLayout.childCount) {
            val img = rightLinearLayout.getChildAt(index)
            if (img is ImageView) {
                img.imageTintList = ColorStateList.valueOf(color)
            }
        }
    }

    fun setLeftImgColor(color: Int) {
        leftImg.imageTintList = ColorStateList.valueOf(color)
    }

    /**
     * 左侧按钮点击事件
     */
    interface OnClickLeftListener {
        fun leftOnClick(v: View, isBack: Boolean)
    }

    /**
     * 右侧按钮点击事件
     */
    interface OnClickRightListener {
        fun rightOnClick(v: View, position: Int)
    }

}