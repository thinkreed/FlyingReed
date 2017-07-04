package reed.flyingreed.controller.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import reed.flyingreed.R
import reed.flyingreed.component.DataFetcher
import reed.flyingreed.component.Observer
import reed.flyingreed.model.Model
import reed.flyingreed.model.Template
import reed.flyingreed.model.ViewHolder
import reed.flyingreed.mvvm.ViewGroupManager
import reed.flyingreed.mvvm.viewmanagers.BaseViewManager
import reed.flyingreed.mvvm.viewmanagers.CommandViewManager

/**
 * Created by thinkreed on 2017/6/17.
 */

class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Observer{


    private lateinit var data:MutableList<Model>

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        DataFetcher.registerObserver(this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolder) {
            holder.viewGroupManager.bind(data[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (Template.values()[viewType]) {
            Template.ITEM_VIDEO -> ViewHolder(ViewGroupManager(parent, R.layout.item_video)
                    .add(R.id.artist, BaseViewManager())
                    .add(R.id.avatar, BaseViewManager())
                    .add(R.id.cover, BaseViewManager())
                    .add(0, CommandViewManager())
            )
            else -> throw IllegalArgumentException("not support type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].template.ordinal
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        DataFetcher.unregisterObserver(this)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onDataArrived(models: MutableList<Model>) {
        data = models
    }
}
