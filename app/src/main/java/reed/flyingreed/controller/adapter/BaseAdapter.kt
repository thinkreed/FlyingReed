package reed.flyingreed.controller.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import reed.flyingreed.component.Observer
import reed.flyingreed.model.ViewHolder
import reed.flyingreed.mvvm.ViewModel


/**
 * Created by thinkreed on 2017/6/17.
 */

abstract class BaseAdapter<T> : RecyclerView.Adapter<ViewHolder<T>>(), Observer {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<T> {
        return ViewHolder(onCreateViewModel(parent, viewType))
    }

    override fun onBindViewHolder(holder: ViewHolder<T>?, position: Int) {
        holder?.viewModel?.bind(holder, position)
    }

    abstract fun onCreateViewModel(parent: ViewGroup?, viewType: Int): ViewModel<T>

    override fun getItemCount(): Int {
        return 0
    }
}
