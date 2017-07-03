package reed.flyingreed.controller.activity

import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.MotionEventCompat
import android.view.GestureDetector
import android.view.MotionEvent
import org.greenrobot.eventbus.EventBus
import reed.flyingreed.mvvm.Events.FlingEvents

/**
 * Created by thinkreed on 2017/7/1.
 */

abstract class DetectorActivity : FragmentActivity() {

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?,
                                 velocityX: Float, velocityY: Float): Boolean {
                val dx = MotionEventCompat.getAxisValue(e1, MotionEvent.AXIS_X) -
                        MotionEventCompat.getAxisValue(e2, MotionEvent.AXIS_X)
                val dy = MotionEventCompat.getAxisValue(e1, MotionEvent.AXIS_Y) -
                        MotionEventCompat.getAxisValue(e2, MotionEvent.AXIS_Y)
                if (Math.abs(dy) < Math.abs(dx)) {
                    if (velocityX > 0) {
                        EventBus.getDefault().post(FlingEvents(FlingEvents.RIGHT,
                                this@DetectorActivity::class))
                    } else {
                        EventBus.getDefault().post(FlingEvents(FlingEvents.LEFT,
                                this@DetectorActivity::class))
                    }
                } else {
                    if (velocityY > 0) {
                        onBackPressed()
                    } else {
                        EventBus.getDefault().post(FlingEvents(FlingEvents.UP,
                                this@DetectorActivity::class))
                    }
                }
                return true
            }

        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.mDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}