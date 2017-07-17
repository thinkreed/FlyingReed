package reed.flyingreed.widget

import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import reed.flyingreed.KotlinApplication


import reed.flyingreed.controller.adapter.ListAdapter
import java.util.*


/**
 * Created by thinkreed on 2017/7/5.
 */

class BaseScrollListener : RecyclerView.OnScrollListener() {

    private val lastVisibles = IntArray(2, { -1 })

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
        val adapter: ListAdapter = recyclerView?.adapter as ListAdapter
        when (layoutManager) {
            is LinearLayoutManager -> {

            }
            is StaggeredGridLayoutManager -> {

                val firsts = IntArray(2, { -1 })
                layoutManager.findFirstVisibleItemPositions(firsts)
                val first = firsts[0]
                val last = lastVisibles[0]
                val increase = (first > last)
                val visibleCount = layoutManager.findLastVisibleItemPositions(firsts)[0] - first
//                if (first > last) {
//                    preload(first + visibleCount, increase, adapter)
//                } else {
//                    preload(first, increase, adapter)
//                }
                Glide.with(KotlinApplication.instance).`as`(ReedAppModule.Size::class.java)
                        .load(adapter.getData(5).cover)
                        .into(object : SimpleTarget<ReedAppModule.Size>() {
                            override fun onResourceReady(resource: ReedAppModule.Size?,
                                                         transition: Transition<in ReedAppModule.Size>?) {
                                Log.d("thinkreed", String.format(Locale.ROOT, "%dx%d",
                                        resource?.width, resource?.height))
                            }

                        })
            }
            else -> throw IllegalArgumentException("not a supported layout manager")
        }
    }

    private fun computeSize(prefered:Int, uri: Uri) {

    }

    private fun preload(start: Int, increase: Boolean, adapter: ListAdapter) {
        if (start >= 5 && start < adapter.itemCount - 5) {
            preload(start, start + if (increase) 5 else -5, adapter)
        }
    }

    private fun preload(from: Int, to: Int, adapter: ListAdapter) {
        for (i in from..to) {

        }
    }
}