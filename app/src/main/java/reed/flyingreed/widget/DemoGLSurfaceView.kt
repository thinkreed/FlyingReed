package reed.flyingreed.widget

import android.content.Context
import android.opengl.GLSurfaceView

/**
 * Created by thinkreed on 2017/7/25.
 */
class DemoGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val mRenderer = DemoGLRender()

    init {
        setEGLContextClientVersion(2)
        setRenderer(mRenderer)
    }
}