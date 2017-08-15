package reed.flyingreed.common.widget

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import android.view.View
import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * Created by thinkreed on 2017/7/8.
 */
class TextureRenderView(context: Context) : IRenderView, TextureView(context) {

    private var mSurfaceTexture: SurfaceTexture? = null
    private val mSHCallbacks by lazy {
        mutableListOf<SurfaceViewRender.SHCallback>()
    }

    init {
        surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?,
                                                     width: Int, height: Int) {
                mSurfaceTexture = surface
                for (shCallback in mSHCallbacks) {
                    shCallback.surfaceChanged(Surface(mSurfaceTexture), PixelFormat.OPAQUE, width, height)
                }
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                mSurfaceTexture = surface

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return true
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?,
                                                   width: Int, height: Int) {
                mSurfaceTexture = surface
                for (shCallback in mSHCallbacks) {
                    shCallback.surfaceCreated(Surface(mSurfaceTexture))
                }
            }
        }
    }

    override fun bindToPlayer(surface: Surface, mp: IMediaPlayer) {
        mp.setSurface(Surface(mSurfaceTexture))
    }

    override fun addSHCallback(shCallback: SurfaceViewRender.SHCallback) {
        mSHCallbacks.add(shCallback)
    }

    override fun removeSHCallback(shCallback: SurfaceViewRender.SHCallback) {
        mSHCallbacks.remove(shCallback)
    }

    override fun getView(): View {
        return this
    }
}