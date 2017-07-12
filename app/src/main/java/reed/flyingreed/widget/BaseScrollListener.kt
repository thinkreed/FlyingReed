package reed.flyingreed.widget

import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide


/**
 * Created by thinkreed on 2017/7/5.
 */

class BaseScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                Glide.with(recyclerView?.context).resumeRequestsRecursive()
            }
            else -> {
                Glide.with(recyclerView?.context).pauseRequestsRecursive()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }
}