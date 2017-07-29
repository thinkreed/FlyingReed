package reed.flyingreed.controller.activity

import android.app.ActivityManager
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import reed.flyingreed.widget.DragLayout

/**
 * Created by thinkreed on 2017/7/23.
 */
abstract class DragActivity : FragmentActivity() {

    private lateinit var mDragLayout: DragLayout
    private val mRect by lazy {
        Rect()
    }
    private val mActivityManager by lazy {
        this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDragLayout = DragLayout(this)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startLockTask()
        }
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(mDragLayout)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mDragLayout.setDragContent(LayoutInflater.from(this).inflate(layoutResID, null),
                { top, _ ->
                    mRect.setEmpty()
                    windowManager.defaultDisplay.getRectSize(mRect)
                    mRect.top = top
                })
    }
}