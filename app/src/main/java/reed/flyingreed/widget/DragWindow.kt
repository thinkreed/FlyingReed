package reed.flyingreed.widget

import android.content.Context
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

/**
 * Created by thinkreed on 2017/7/30.
 */
class DragWindow(context: Context, layoutId: Int) {

    private val mWindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val mContentView: View

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mContentView = inflater.inflate(layoutId, null, false)
    }

    fun show(view: View) {
        mWindowManager.addView(mContentView, createLayoutParams(view.windowToken))
    }

    private fun createLayoutParams(token: IBinder): WindowManager.LayoutParams {
        val p = WindowManager.LayoutParams()
        p.token = token
        return p
    }
}