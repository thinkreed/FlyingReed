package reed.flyingreed.widget


import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.PopupWindow
import android.view.MotionEvent
import android.support.v4.view.MotionEventCompat
import android.view.WindowManager

/**
 * Created by thinkreed on 2017/7/30.
 */
class DragWindow(contentView: View, width: Int, height: Int, focusable: Boolean)
    : PopupWindow(contentView, width, height, focusable) {

    private lateinit var mDragLayout: DragLayout
    private var mCurX = 0f
    private var mCurY = 0f
    private var mCurWidth = 0
    private var mCurHeight = 0
    private var mTotalDy = 0f

    init {
        val wm = contentView.context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        mCurHeight = size.y
        mCurWidth = size.x
        this.setTouchInterceptor { v, event ->
            when (MotionEventCompat.getActionMasked(event)) {
                MotionEvent.ACTION_DOWN -> {
                    mCurX = MotionEventCompat.getAxisValue(event,
                            MotionEventCompat.AXIS_X)
                    mCurY = MotionEventCompat.getAxisValue(event,
                            MotionEventCompat.AXIS_Y)
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = MotionEventCompat.getAxisValue(event,
                            MotionEventCompat.AXIS_X) - mCurX
                    val dy = MotionEventCompat.getAxisValue(event,
                            MotionEventCompat.AXIS_Y) - mCurY
                    mTotalDy = Math.min(Math.max(mTotalDy + dy, 0f), mCurHeight.toFloat())
                    val percent = mTotalDy / mCurHeight
                    val w = (mCurWidth * (1 - percent)).toInt()
                    val h = (mCurHeight * (1 - percent)).toInt()
                    update(w, h)
                }
                MotionEvent.ACTION_CANCEL -> {

                }
            }
            return@setTouchInterceptor false
        }
        setBackgroundDrawable(ColorDrawable(Color.RED))
    }

    override fun setContentView(contentView: View?) {
        contentView?.let {
//            mDragLayout = DragLayout(contentView.context)
//            mDragLayout.setDragContent(contentView, {
//                percentage, x, y ->
//
//            })
            super.setContentView(contentView)
        }
    }
}