package reed.flyingreed.widget

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide


/**
 * Created by thinkreed on 2017/7/5.
 */

class BaseScrollListener : RecyclerView.OnScrollListener() {

    private var lastVisibles:IntArray? = null

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
        val layoutManager = recyclerView?.layoutManager
        when (layoutManager) {
            is LinearLayoutManager -> {
                if (lastVisibles == null) {
                    lastVisibles = IntArray(1, {-1})
                }
            }
            is StaggeredGridLayoutManager -> {
                if (lastVisibles == null) {
                    lastVisibles = IntArray(layoutManager.spanCount, {-1})
                }
                val firsts = intArrayOf()
                layoutManager.findFirstVisibleItemPositions(firsts)
            }
            else -> throw IllegalArgumentException("not a supported layout manager")
        }
    }
}