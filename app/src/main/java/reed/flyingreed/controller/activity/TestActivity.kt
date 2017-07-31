package reed.flyingreed.controller.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.fragment_empty.*
import reed.flyingreed.R
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.WindowManager.LayoutParams
import reed.flyingreed.widget.DragWindow


/**
 * Created by thinkreed on 2017/7/30.
 */

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_empty)
        val contentView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_share, null)
        val pop = DragWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true)
        pop.isTouchable = true
        pop.isOutsideTouchable = true
        message.text = "test"
        message.setOnClickListener {
            //            pop.showAtLocation(pop.contentView, Gravity.NO_GRAVITY, 0, 0)
            pop.showAsDropDown(message)
        }
    }
}