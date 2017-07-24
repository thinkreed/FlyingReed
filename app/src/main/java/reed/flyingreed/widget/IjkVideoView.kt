package reed.flyingreed.widget

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.FrameLayout
import android.widget.MediaController
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import kotlin.properties.Delegates


class IjkVideoView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
        SurfaceViewRender.SHCallback, MediaController.MediaPlayerControl {

    private val mRenderView: IRenderView
    private val mMediaPlayer = IjkMediaPlayer()
    private val mAttrs = attrs
    private var mBufferPercentage = -1
    private lateinit var mMediaControllor: MediaController
    private var mState by Delegates.observable(State.IDLE) {
        property, oldValue, newValue ->
        when (newValue) {
            State.PLAYING -> {
                mMediaPlayer.start()
                mMediaControllor.show()
            }
            State.PAUSED -> {
                mMediaPlayer.pause()
                mMediaControllor.hide()
            }
            State.IDLE -> {
                mMediaPlayer.stop()
                mMediaControllor.hide()
            }
        }
    }

    init {
        mRenderView = initRenderView(RenderType.TEXTURE)
        mRenderView.addSHCallback(this)
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setOnPreparedListener {
            mMediaPlayer.start()
            mMediaControllor.show()
        }
        mMediaPlayer.setOnBufferingUpdateListener { _, percentage ->
            mBufferPercentage = percentage
        }
        addView(mRenderView.getView())
    }

    override fun surfaceChanged(surface: Surface?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(surface: Surface?) {
    }

    override fun surfaceCreated(surface: Surface?) {
        if (surface != null) {
            mMediaPlayer.setSurface(surface)
            mMediaPlayer.prepareAsync()
        }
    }

    override fun onDetachedFromWindow() {
        mMediaPlayer.stop()
        mMediaPlayer.reset()
        mMediaPlayer.release()
        mRenderView.removeSHCallback(this)
        super.onDetachedFromWindow()
    }

    override fun isPlaying(): Boolean {
        return mMediaPlayer.isPlaying
    }

    override fun pause() {
        mState = State.PAUSED
    }

    override fun start() {
        mState = State.PLAYING
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getDuration(): Int {
        return mMediaPlayer.duration.toInt()
    }

    override fun getBufferPercentage(): Int {
        return mBufferPercentage
    }

    override fun seekTo(pos: Int) {
        mMediaPlayer.seekTo(pos.toLong())
    }

    override fun getCurrentPosition(): Int {
        return mMediaPlayer.currentPosition.toInt()
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        return mMediaPlayer.audioSessionId
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mMediaControllor.isShowing) mMediaControllor.hide() else mMediaControllor.show()
        return super.onTouchEvent(event)
    }

    fun setMediaController(mediaController: MediaController) {
        mMediaControllor = mediaController
        mMediaControllor.setMediaPlayer(this)
        mMediaControllor.setAnchorView(this)
    }

    fun setVideoPath(path: String) {
        mMediaPlayer.dataSource = path
    }

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