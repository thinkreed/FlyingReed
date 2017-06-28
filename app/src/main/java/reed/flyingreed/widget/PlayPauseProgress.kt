package reed.flyingreed.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView

import android.widget.Scroller
import com.bumptech.glide.Glide

import reed.flyingreed.R
import kotlinx.android.synthetic.main.control_play_pause.view.*

/**
 * Created by thinkreed on 2017/6/28.
 */
class PlayPauseProgress(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val mProgressPaint: Paint
    private val mCirclePaint: Paint

    private var mStrokeWidth: Float

    private val mScroller: Scroller
    private var mSweepAngle: Float
    private val mRectF: RectF
    private val mCenter: PointF = PointF()
    private var mRadius = 0f
    private var mState = State.INITIAL
    private var mSweeppedAngle = 0f
    private var mPlayImage: ImageView

    init {
        setWillNotDraw(false)
        LayoutInflater.from(context).inflate(R.layout.control_play_pause, this, true)
        mPlayImage = center_image
        Glide.with(context).load(R.mipmap.ic_launcher).into(mPlayImage)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PlayPauseProgress, 0, 0)
        mStrokeWidth = a.getDimension(R.styleable.PlayPauseProgress_stroke_width, 0f)
        mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressPaint.color = resources.getColor(R.color.colorAccent)
        mProgressPaint.strokeWidth = mStrokeWidth
        mProgressPaint.style = Paint.Style.STROKE
        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaint.color = resources.getColor(R.color.colorGrey)
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = mStrokeWidth
        mSweepAngle = 0f
        mRectF = RectF()
        mScroller = Scroller(context, null, true)
        a.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            when (mState) {
                State.INITIAL -> {
                    canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mCirclePaint)
                }
                State.UPDATING -> {
                    canvas.drawArc(mRectF, mSweeppedAngle + 270f, mSweepAngle, false, mProgressPaint)
                }
                State.IDLE -> {

                }
                else -> return@let
            }

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if ((w != oldw) or (h != oldh)) {
            mCenter.x = w.toFloat() / 2
            mCenter.y = h.toFloat() / 2
            mRadius = w.toFloat() / 4
            mRectF.left = mCenter.x - mRadius
            mRectF.right = mCenter.x + mRadius
            mRectF.top = mCenter.y - mRadius
            mRectF.bottom = mCenter.y + mRadius
        }
    }

    fun setProgress(progress: Float) {
        val curAngle = progress * 360f
        mSweepAngle = curAngle - mSweeppedAngle
        mSweeppedAngle = curAngle
    }

    fun setState(state: State) {
        mState = state
        when (mState) {
            State.IDLE -> {

            }
            State.UPDATING -> {

            }
            else -> return
        }
    }

    enum class State {
        INITIAL, UPDATING, IDLE
    }

}