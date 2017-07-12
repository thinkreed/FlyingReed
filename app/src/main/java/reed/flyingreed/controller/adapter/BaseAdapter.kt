package reed.flyingreed.controller.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import reed.flyingreed.component.DataFetcher
import reed.flyingreed.component.Observer
import reed.flyingreed.model.ViewHolder
import reed.flyingreed.mvvm.ViewModel
import reed.flyingreed.widget.BaseScrollListener


/**
 * Created by thinkreed on 2017/6/17.
 */

abstract class BaseAdapter<T> : RecyclerView.Adapter<ViewHolder<T>>(), Observer {

    protected var data = mutableListOf<T>()
    private val mOnScrollListener by lazy {
        BaseScrollListener()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView?.addOnScrollListener(mOnScrollListener)
        DataFetcher.registerObserver(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<T> {
        return ViewHolder(onCreateViewModel(parent, viewType))
    }

    override fun onBindViewHolder(holder: ViewHolder<T>?, position: Int) {
        holder?.viewModel?.model = data[position]
    }

    abstract fun onCreateViewModel(parent: ViewGroup?, viewType: Int): ViewModel<T>

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        DataFetcher.unregisterObserver(this)
        recyclerView?.removeOnScrollListener(mOnScrollListener)
        super.onDetachedFromRecyclerView(recyclerView)
    }
}
