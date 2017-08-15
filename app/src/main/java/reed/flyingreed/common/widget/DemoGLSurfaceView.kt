package reed.flyingreed.common.widget

import android.content.Context
import android.opengl.GLSurfaceView

/**
 * Created by thinkreed on 2017/7/25.
 */
class DemoGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val mRenderer = DemoGLRender()

    init {
        setEGLContextClientVersion(2)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        setRenderer(mRenderer)
    }
}