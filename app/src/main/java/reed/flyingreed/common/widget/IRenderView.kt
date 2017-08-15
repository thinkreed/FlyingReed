package reed.flyingreed.common.widget

import android.view.Surface
import android.view.View
import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * Created by thinkreed on 2017/7/8.
 */

interface IRenderView {
    fun bindToPlayer(surface: Surface, mp: IMediaPlayer)
    fun addSHCallback(shCallback: SurfaceViewRender.SHCallback)
    fun removeSHCallback(shCallback: SurfaceViewRender.SHCallback)
    fun getView(): View
}