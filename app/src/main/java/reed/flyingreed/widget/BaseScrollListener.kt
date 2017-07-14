package reed.flyingreed.widget

import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import reed.flyingreed.KotlinApplication


/**
 * Created by thinkreed on 2017/7/5.
 */

class BaseScrollListener : RecyclerView.OnScrollListener() {

    private val target by lazy {
        PreloadTarget.obtain<Bitmap>(Glide.with(KotlinApplication.instance), 0, 0)
    }

    private val lastVisibles = intArrayOf(-1)

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

            }
            is StaggeredGridLayoutManager -> {

                val firsts = intArrayOf()
                layoutManager.findFirstVisibleItemPositions(firsts)
                val first = firsts[0]
                val last = lastVisibles[0]
                val increase = (first > last)
                preload(first, )
            }
            else -> throw IllegalArgumentException("not a supported layout manager")
        }
    }

    private fun preload(firstVisible: Int, increase: Boolean) {

    }

    class PreloadTarget<Z> private constructor(private val requestManager: RequestManager
                                               , width: Int, height: Int)
        : SimpleTarget<Z>(width, height) {

        override fun onResourceReady(resource: Z, transition: Transition<in Z>) {
            requestManager.clear(this)
        }

        companion object {

            /**
             * Returns a PreloadTarget.

             * @param width  The width in pixels of the desired resource.
             * *
             * @param height The height in pixels of the desired resource.
             * *
             * @param <Z>    The type of the desired resource.
            </Z> */
            fun <Z> obtain(requestManager: RequestManager, width: Int, height: Int): PreloadTarget<Z> {
                return PreloadTarget(requestManager, width, height)
            }
        }
    }
}