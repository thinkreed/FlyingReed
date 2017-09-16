package reed.flyingreed.common.activity

import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.MotionEventCompat
import android.view.GestureDetector
import android.view.MotionEvent
import org.greenrobot.eventbus.EventBus
import reed.flyingreed.common.presenter.Events.FlingEvents

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

        val dx = MotionEventCompat.getAxisValue(e1,
            MotionEvent.AXIS_X) - MotionEventCompat.getAxisValue(e2, MotionEvent.AXIS_X)
        val dy = MotionEventCompat.getAxisValue(e1,
            MotionEvent.AXIS_Y) - MotionEventCompat.getAxisValue(e2, MotionEvent.AXIS_Y)

        if (isFlingRight(dy, dx, velocityX)) {
          EventBus.getDefault().post(FlingEvents(FlingEvents.RIGHT,
              this@DetectorActivity::class))
          return true
        }

        if (isFlingLeft(dy, dx, velocityX)) {
          EventBus.getDefault().post(FlingEvents(FlingEvents.LEFT,
              this@DetectorActivity::class))
          return true
        }

        if (isFlingDown(dy, dx, velocityY)) {
          onBackPressed()
          return true
        }

        if (isFlingUp(dy, dx, velocityY)) {
          EventBus.getDefault().post(FlingEvents(FlingEvents.UP,
              this@DetectorActivity::class))
          return true
        }
        return true
      }

    })
  }

  private fun isFlingUp(dy: Float, dx: Float, velocityY: Float) =
      Math.abs(dy) >= Math.abs(dx) && velocityY <= 0

  private fun isFlingDown(dy: Float, dx: Float, velocityY: Float) =
      Math.abs(dy) >= Math.abs(dx) && velocityY > 0

  private fun isFlingLeft(dy: Float, dx: Float, velocityX: Float) =
      Math.abs(dy) < Math.abs(dx) && velocityX <= 0

  private fun isFlingRight(dy: Float, dx: Float, velocityX: Float) =
      Math.abs(dy) < Math.abs(dx) && velocityX > 0

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    this.mDetector.onTouchEvent(event)
    return super.onTouchEvent(event)
  }
}