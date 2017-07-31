package reed.flyingreed.widget

import android.content.Context
import android.graphics.Point
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import java.util.*
import java.util.concurrent.Callable

/**
 * Created by thinkreed on 2017/7/23.
 */
class DragLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var mVerticalRange = 0
    private var mHorizontalRange = 0
    private var mViewPositionChanged: ((Float, Int, Int) -> Unit)? = null
    private lateinit var mDragHelper: ViewDragHelper

    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        mVerticalRange = size.y
        mHorizontalRange = size.x
    }

    constructor(context: Context) : this(context, null)

    fun setDragContent(contentView: View, onViewPositionChanged: (Float, Int, Int) -> Unit) {
        mDragHelper = ViewDragHelper.create(this, object
            : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View?, pointerId: Int): Boolean {
                return true
            }

            override fun onViewCaptured(capturedChild: View?, activePointerId: Int) {
                super.onViewCaptured(capturedChild, activePointerId)
            }

            override fun onViewPositionChanged(changedView: View?, left: Int,
                                               top: Int, dx: Int, dy: Int) {
                val percentage = top.toFloat() / mVerticalRange
                val x = mHorizontalRange * percentage / 2
                val y = mVerticalRange * percentage / 2
                this@DragLayout.x = x
                this@DragLayout.y = y
                this@DragLayout.scaleX = 1 - percentage
                this@DragLayout.scaleY = 1 - percentage
                mViewPositionChanged?.invoke(percentage,
                        contentView.width, contentView.height)
            }

            override fun getViewHorizontalDragRange(child: View?): Int {
                return 0
            }

            override fun getViewVerticalDragRange(child: View?): Int {
                return mVerticalRange
            }

            override fun clampViewPositionHorizontal(child: View?, left: Int, dx: Int): Int {
//            val leftBound = 0
//            val rightBound = mDragContent.width
//
//            return Math.min(Math.max(left, leftBound), rightBound)
                return 0
            }

            override fun clampViewPositionVertical(child: View?, top: Int, dy: Int): Int {
                val topBound = 0
                val bottomBound = getViewVerticalDragRange(child)

                return Math.min(Math.max(top, topBound), bottomBound)
            }
        })
        mViewPositionChanged = onViewPositionChanged
        addView(contentView)
    }

    override fun computeScroll() {
        if (mDragHelper?.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return mDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)
    }
}