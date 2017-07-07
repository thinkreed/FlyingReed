package reed.flyingreed.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * Created by thinkreed on 2017/7/6.
 */

class IjkVideoView(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
    : SurfaceView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var mSurfaceHolder: SurfaceHolder
    private val mPlayer = IjkMediaPlayer()

    init {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                holder?.let {
                    mSurfaceHolder = holder
                }
            }
        })
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : this(context, attrs, defStyleAttr, 0)

    fun openVideo(uri: String) {
        mPlayer.setDisplay(mSurfaceHolder)
        mPlayer.dataSource = uri
        mPlayer.setOnCompletionListener {
            mPlayer.start()
        }
        mPlayer.prepareAsync()
    }
}