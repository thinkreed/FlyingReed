package reed.flyingreed.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

import android.widget.Scroller
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init

import reed.flyingreed.R
import kotlinx.android.synthetic.main.control_play_pause.view.*

/**
 * Created by thinkreed on 2017/6/28.
 */
class PlayPauseProgress(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val mProgressPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private val mCirclePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private var mStrokeWidth = 0f

    private lateinit var mScroller: Scroller
    private val mRectF by lazy { RectF() }
    private var mState = State.UPDATING
    private var mSweeppedAngle = 0f
    private lateinit var mPlayImage: View
    private lateinit var mContext: Context
    private lateinit var mOnStateChangeListener: OnStateChangeListener
    private val mLeftTopPoint by lazy {
        PointF()
    }
    private val mLeftBottomPoint by lazy {
        PointF()
    }
    private val mRightTopPoint by lazy {
        PointF()
    }
    private val mRightBottomPoint by lazy {
        PointF()
    }
    private val mRightCenterPoint by lazy {
        PointF()
    }
    private val mPath by lazy { Path() }

    init {
        if (!isInEditMode) {
            setWillNotDraw(false)
            LayoutInflater.from(context).inflate(R.layout.control_play_pause, this, true)
            mPlayImage = center_image
            mPlayImage.setOnClickListener {
                when (mState) {
                    State.UPDATING -> {
                        mState = State.IDLE
                    }
                    State.IDLE -> {
                        mState = State.UPDATING
                    }
                    else -> return@setOnClickListener
                }
                postInvalidate()
                mOnStateChangeListener.onStateChanged(mState)
            }
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PlayPauseProgress, 0, 0)
            mStrokeWidth = a.getDimension(R.styleable.PlayPauseProgress_stroke_width, 0f)
            mProgressPaint.color = resources.getColor(R.color.colorAccent)
            mProgressPaint.strokeWidth = mStrokeWidth
            mProgressPaint.style = Paint.Style.STROKE
            mCirclePaint.color = resources.getColor(R.color.colorGrey)
            mCirclePaint.style = Paint.Style.STROKE
            mCirclePaint.strokeWidth = mStrokeWidth
            mContext = context
            mScroller = Scroller(context, null, true)
            a.recycle()
        }

    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            canvas.drawArc(mRectF, 270f + mSweeppedAngle,
                    360f - mSweeppedAngle, false, mCirclePaint)
            canvas.drawArc(mRectF, 270f, mSweeppedAngle, false, mProgressPaint)
            when (mState) {
                State.UPDATING -> {
                    canvas.drawLine(mLeftTopPoint.x, mLeftTopPoint.y, mLeftBottomPoint.x,
                            mLeftBottomPoint.y, mProgressPaint)
                    canvas.drawLine(mRightTopPoint.x, mRightTopPoint.y, mRightBottomPoint.x,
                            mRightBottomPoint.y, mProgressPaint)
                }
                State.IDLE -> {
                    mPath.moveTo(mLeftTopPoint.x, mLeftTopPoint.y)
                    mPath.lineTo(mLeftBottomPoint.x, mLeftBottomPoint.y)
                    mPath.lineTo(mRightCenterPoint.x, mRightCenterPoint.y)
                    mPath.lineTo(mLeftTopPoint.x, mLeftTopPoint.y)
                    canvas.drawPath(mPath, mProgressPaint)
                }
                else -> return
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val r = if (w > h) h / 4 else w / 4
        for (i in 0 until childCount) {
            getChildAt(i).measure(ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, r),
                    ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, r))
        }
        setMeasuredDimension(2 * r, 2 * r)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mRectF.left = mStrokeWidth
        mRectF.right = right - left - mStrokeWidth
        mRectF.top = mStrokeWidth
        mRectF.bottom = bottom - top - mStrokeWidth

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val width = child.measuredWidth
            val height = child.measuredHeight
            val cl = (right - left - width) / 2
            val ct = (bottom - top - height) / 2
            mLeftTopPoint.x = (cl + width / 4).toFloat()
            mLeftTopPoint.y = (ct + height / 4).toFloat()
            mLeftBottomPoint.x = (cl + width / 4).toFloat()
            mLeftBottomPoint.y = (ct + height / 4 * 3).toFloat()
            mRightTopPoint.x = (cl + width / 4 * 3).toFloat()
            mRightTopPoint.y = (ct + height / 4).toFloat()
            mRightCenterPoint.x = (cl + width / 4 * 3).toFloat()
            mRightCenterPoint.y = (ct + width / 2).toFloat()
            mRightBottomPoint.x = (cl + width / 4 * 3).toFloat()
            mRightBottomPoint.y = (ct + height / 4 * 3).toFloat()
            child.layout(cl, ct, cl + width, ct + height)
        }
    }

    fun setProgress(progress: Float) {
        if (progress == 100f) {
            mState = State.IDLE
        }
        mSweeppedAngle = progress * 360f
        postInvalidate()
    }

    fun setOnStateChangeListener(listener: OnStateChangeListener) {
        mOnStateChangeListener = listener
    }

    enum class State {
        UPDATING, IDLE
    }

    interface OnStateChangeListener {
        fun onStateChanged(state: State): Unit
    }

}