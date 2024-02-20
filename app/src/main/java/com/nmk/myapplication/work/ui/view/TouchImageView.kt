package com.nmk.myapplication.work.ui.view

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs

/**
 * @desc: 缩放图片控件
 * @author：Created by LinManyi on 2024/2/20
 */
class TouchImageView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {
    lateinit var mMatrix: Matrix
    var mode = NONE

    // 记住一些关于缩放的
    private var last = PointF()
    private var start = PointF()
    var minScale = 1f
    var maxScale = 3f
    lateinit var m: FloatArray
    var viewWidth = 0
    var viewHeight = 0
    var saveScale = 1f
    var origWidth = 0f
    var origHeight = 0f
    private var oldMeasuredWidth = 0
    private var oldMeasuredHeight = 0

    //缩放手势检测
    private lateinit var mScaleDetector: ScaleGestureDetector

    //默认构造函数
    init {
        sharedConstructing()
    }

    private fun sharedConstructing() {
        super.setClickable(true)
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mMatrix = matrix
        m = FloatArray(9)
        imageMatrix = mMatrix
        scaleType = ScaleType.MATRIX
        setOnTouchListener { v, event ->
            mScaleDetector.onTouchEvent(event)
            val curr = PointF(event.x, event.y)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    last.set(curr) //当前位置
                    start.set(last) //开始点
                    mode = DRAG
                }

                MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                    val deltaX = curr.x - last.x
                    val deltaY = curr.y - last.y

                    //拖拽位置
                    val fixTransX = getFixDragTrans(deltaX, viewWidth.toFloat(), origWidth * saveScale)
                    val fixTransY = getFixDragTrans(deltaY, viewHeight.toFloat(), origHeight * saveScale)
                    mMatrix.postTranslate(fixTransX, fixTransY)
                    fixTrans()
                    last[curr.x] = curr.y
                }

                MotionEvent.ACTION_UP -> {
                    mode = NONE
                    val xDiff = abs(curr.x - start.x).toInt()
                    val yDiff = abs(curr.y - start.y).toInt()
                    if (xDiff < CLICK && yDiff < CLICK) performClick()
                }

                MotionEvent.ACTION_POINTER_UP -> mode = NONE
            }
            imageMatrix = mMatrix
            invalidate()
            true // 指示事件已处理
        }
    }

    fun setMaxZoom(x: Float) {
        maxScale = x
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
//            mode = ZOOM
//            return true
//        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor: Float = detector.getScaleFactor()
            val origScale = saveScale
            saveScale *= mScaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }
            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) mMatrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2f, viewHeight / 2f) else mMatrix.postScale(
                mScaleFactor,
                mScaleFactor,
                detector.getFocusX(),
                detector.getFocusY()
            )
            fixTrans()
            return true
        }
    }

    fun fixTrans() {
        mMatrix.getValues(m)
        val transX = m[Matrix.MTRANS_X]
        val transY = m[Matrix.MTRANS_Y]
        val fixTransX = getFixTrans(transX, viewWidth.toFloat(), origWidth * saveScale)
        val fixTransY = getFixTrans(transY, viewHeight.toFloat(), origHeight * saveScale)
        if (fixTransX != 0f || fixTransY != 0f) mMatrix.postTranslate(fixTransX, fixTransY)
    }

    fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) return -trans + minTrans
        return if (trans > maxTrans) -trans + maxTrans else 0f
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        return if (contentSize <= viewSize) {
            0f
        } else delta
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        //
        // 旋转时重新缩放图像
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight || viewWidth == 0 || viewHeight == 0) return
        oldMeasuredHeight = viewHeight
        oldMeasuredWidth = viewWidth
        if (saveScale == 1f) {
            //Fit to screen.
            val scale: Float

            //获取图片
            val drawable: Drawable? = drawable
            if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
            val bmWidth: Int = drawable.getIntrinsicWidth()
            val bmHeight: Int = drawable.getIntrinsicHeight()
            val scaleX = viewWidth.toFloat() / bmWidth.toFloat()
            val scaleY = viewHeight.toFloat() / bmHeight.toFloat()
            scale = Math.min(scaleX, scaleY)
            mMatrix.setScale(scale, scale)

            // 将图像居中
            var redundantYSpace = viewHeight.toFloat() - scale * bmHeight.toFloat()
            var redundantXSpace = viewWidth.toFloat() - scale * bmWidth.toFloat()
            redundantYSpace /= 2f
            redundantXSpace /= 2f
            mMatrix.postTranslate(redundantXSpace, redundantYSpace)
            origWidth = viewWidth - 2 * redundantXSpace
            origHeight = viewHeight - 2 * redundantYSpace
            imageMatrix = mMatrix
        }
        fixTrans()
    }

    // 用于标记滑动的坐标
    private var downX = 0
    private val downY = 0
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - downX
                val deltaY = y - downX
                //左右滑动
                if (Math.abs(deltaX) > Math.abs(deltaY) * 50) {
                    //拦截事件分发
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        //赋值
        downX = x
        downX = y
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        // We can be in one of these 3 states
        const val NONE = 0 //初始
        const val DRAG = 1 //拖动
        const val ZOOM = 2 //放大
        const val CLICK = 3
    }
}
