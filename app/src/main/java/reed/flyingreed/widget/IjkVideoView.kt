package reed.flyingreed.widget

import android.content.Context
import android.media.AudioManager
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.widget.FrameLayout
import tv.danmaku.ijk.media.player.IjkMediaPlayer


class IjkVideoView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
        SurfaceViewRender.SHCallback {



    private val mRenderView: IRenderView
    private val mMediaPlayer = IjkMediaPlayer()
    private val mAttrs = attrs

    init {
        mRenderView = initRenderView(RenderType.SURFACE)
        mRenderView.addSHCallback(this)
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setOnPreparedListener {
            mMediaPlayer.start()
        }
        addView(mRenderView.getView())
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (holder != null) {
            mMediaPlayer.setDisplay(holder)
            mMediaPlayer.prepareAsync()
        }
    }

    override fun onDetachedFromWindow() {
        mMediaPlayer.stop()
        mMediaPlayer.reset()
        mRenderView.removeSHCallback(this)
        super.onDetachedFromWindow()
    }

    fun setVideoPath(path: String) {
        mMediaPlayer.dataSource = path
    }

    fun isPlaying(): Boolean = mMediaPlayer.isPlaying

    fun pause() = mMediaPlayer.pause()

    fun start() = mMediaPlayer.start()

    private fun initRenderView(renderType: RenderType): IRenderView {
        return when (renderType) {
            RenderType.SURFACE -> {
                SurfaceViewRender(context, mAttrs)
            }
            RenderType.TEXTURE -> {
                TextureRenderView(context)
            }
        }
    }

    enum class RenderType {
        SURFACE, TEXTURE
    }

    enum class State {
        PLAYING, PAUSED, IDLE
    }
}