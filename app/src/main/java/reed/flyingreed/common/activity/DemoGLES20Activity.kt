package reed.flyingreed.common.activity

import android.app.Activity
import android.os.Bundle
import reed.flyingreed.common.widget.DemoGLSurfaceView

/**
 * Created by thinkreed on 2017/7/25.
 */
class DemoGLES20Activity : Activity() {
    private var mGLView: DemoGLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView
        mGLView = DemoGLSurfaceView(this)
        setContentView(mGLView)
    }
}