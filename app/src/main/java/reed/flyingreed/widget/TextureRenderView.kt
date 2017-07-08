package reed.flyingreed.widget

import android.content.Context
import android.view.SurfaceHolder
import android.view.View
import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * Created by thinkreed on 2017/7/8.
 */
class TextureRenderView(context: Context) : IRenderView, View(context) {
    override fun bindToPlayer(holder: SurfaceHolder, mp: IMediaPlayer) {
    }

    override fun addSHCallback(shCallback: SurfaceViewRender.SHCallback) {
    }

    override fun removeSHCallback(shCallback: SurfaceViewRender.SHCallback) {
    }

    override fun getView(): View {
        return this
    }
}