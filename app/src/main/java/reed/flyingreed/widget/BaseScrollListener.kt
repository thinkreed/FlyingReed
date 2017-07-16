package reed.flyingreed.widget


import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.request.RequestOptions
import reed.flyingreed.KotlinApplication

import reed.flyingreed.controller.adapter.ListAdapter
import java.io.InputStream
import java.nio.charset.Charset
import java.security.MessageDigest


/**
 * Created by thinkreed on 2017/7/5.
 */

class BaseScrollListener : RecyclerView.OnScrollListener() {

    private val sizeRequest by lazy {
        val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        Glide.with(KotlinApplication.instance)
                .applyDefaultRequestOptions(requestOptions)

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
        val adapter: ListAdapter = recyclerView?.adapter as ListAdapter
        when (layoutManager) {
            is LinearLayoutManager -> {

            }
            is StaggeredGridLayoutManager -> {

                val firsts = intArrayOf()
                layoutManager.findFirstVisibleItemPositions(firsts)
                val first = firsts[0]
                val last = lastVisibles[0]
                val increase = (first > last)
                val visibleCount = layoutManager.findLastVisibleItemPositions(firsts)[0] - first
                if (first > last) {
                    preload(first + visibleCount, increase, adapter)
                } else {
                    preload(first, increase, adapter)
                }
            }
            else -> throw IllegalArgumentException("not a supported layout manager")
        }
    }

    private fun preload(start: Int, increase: Boolean, adapter: ListAdapter) {
        if (increase) {
            for (i in 0..5) {
            }
        } else {

        }
    }

    data class Size(val width: Int, val height: Int)

    class MyTrans :BitmapTransformation() {

        val ID = "reed.flyingreed.widget.mytrans"
        val ID_BYTES = ID.toByteArray(Charset.defaultCharset())

        override fun updateDiskCacheKey(messageDigest: MessageDigest?) {
            messageDigest?.update(ID_BYTES)
        }

        override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int)
                : Bitmap {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}