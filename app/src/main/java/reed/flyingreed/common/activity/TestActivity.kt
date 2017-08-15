package reed.flyingreed.common.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_empty.*
import reed.flyingreed.R
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager.LayoutParams
import android.widget.Button


/**
 * Created by thinkreed on 2017/7/30.
 */

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_empty)
        message.text = "this is text activity"
        val floatButton = Button(this)
        floatButton.setTextColor(Color.WHITE)
        floatButton.text = "float button"
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_APPLICATION_PANEL, 0, PixelFormat.TRANSPARENT)
        lp.token = window.decorView.applicationWindowToken
        lp.flags = LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL or
                LayoutParams.FLAG_SHOW_WHEN_LOCKED
        lp.gravity = Gravity.LEFT or Gravity.TOP
        lp.x = 100
        lp.y = 300
        windowManager.addView(floatButton, lp)
    }
}