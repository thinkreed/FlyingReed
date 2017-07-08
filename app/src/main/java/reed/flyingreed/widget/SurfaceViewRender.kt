package reed.flyingreed.widget

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.umeng.socialize.utils.DeviceConfig.context
import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * Created by thinkreed on 2017/7/8.
 */

class SurfaceViewRender(context:Context,  attrs:AttributeSet, defStyleAttr:Int, defStyleRes:Int)
    : SurfaceView(context, attrs, defStyleAttr, defStyleRes), IRenderView {

    interface SHCallback {
        fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int)

        fun surfaceDestroyed(holder: SurfaceHolder?)

        fun surfaceCreated(holder: SurfaceHolder?)
    }

    private val mSHCallbacks by lazy {
        mutableListOf<SHCallback>()
    }

    constructor(context: Context, attrs: AttributeSet):this(context,attrs, 0, 0)

    init {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                for (shCallback in mSHCallbacks) {
                    shCallback.surfaceChanged(holder, format, width, height)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                for (shCallback in mSHCallbacks) {
                    shCallback.surfaceDestroyed(holder)
                }
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                for (shCallback in mSHCallbacks) {
                    shCallback.surfaceCreated(holder)
                }
            }
        })
    }

    override fun bindToPlayer(holder: SurfaceHolder, mp: IMediaPlayer) {
        mp.setDisplay(holder)
    }

    override fun addSHCallback(shCallback: SHCallback) {
        mSHCallbacks.add(shCallback)
    }

    override fun removeSHCallback(shCallback: SHCallback) {
        mSHCallbacks.remove(shCallback)
    }

    override fun getView(): View {
        return this
    }

}