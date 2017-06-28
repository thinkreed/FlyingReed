package reed.flyingreed.controller.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import reed.flyingreed.R
import reed.flyingreed.model.Template
import reed.flyingreed.model.ViewHolder
import reed.flyingreed.mvvm.ViewGroupManager
import reed.flyingreed.mvvm.ViewModel
import reed.flyingreed.mvvm.viewmanagers.BaseViewManager
import reed.flyingreed.mvvm.viewmanagers.CommandViewManager

/**
 * Created by thinkreed on 2017/6/17.
 */

class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var data: ViewModel

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolder) {
            holder.viewGroupManager.bind(data.get(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (Template.values()[viewType]) {
            Template.ITEM_MUSIC_INFO -> ViewHolder(ViewGroupManager(parent, R.layout.item_song)
                    .add(R.id.title, BaseViewManager())
                    .add(R.id.description, BaseViewManager())
                    .add(R.id.cover, BaseViewManager())
                    .add(0, CommandViewManager())
                  )
            else -> throw IllegalArgumentException("not support type")
        }
    }

    override fun getItemCount(): Int {
        return data.dataCount()
    }

    override fun getItemViewType(position: Int): Int {
        return data.get(position).template.ordinal
    }

    fun setViewModel(viewModel: ViewModel) {
        this.data = viewModel
    }
}
