package reed.flyingreed.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import reed.flyingreed.common.component.DataFetcher
import reed.flyingreed.common.component.Observer
import reed.flyingreed.common.model.ViewHolder
import reed.flyingreed.common.mvvm.ViewModel


/**
 * Created by thinkreed on 2017/6/17.
 */

abstract class BaseAdapter<T> : RecyclerView.Adapter<ViewHolder<T>>(), Observer {

    protected var data = mutableListOf<T>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
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

    fun getData(position: Int): T {
        return data[position]
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        DataFetcher.unregisterObserver(this)
        super.onDetachedFromRecyclerView(recyclerView)
    }
}
