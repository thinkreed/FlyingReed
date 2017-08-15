package reed.flyingreed.common.adapter

import android.view.ViewGroup
import reed.flyingreed.R
import reed.flyingreed.common.model.Model
import reed.flyingreed.common.model.Template
import reed.flyingreed.common.mvvm.ViewModel
import reed.flyingreed.common.mvvm.viewmanagers.ActionViewManager
import reed.flyingreed.common.mvvm.viewmanagers.BaseViewManager
import reed.flyingreed.common.mvvm.viewmodels.VideoItemViewModel

/**
 * Created by thinkreed on 2017/7/4.
 */

class ListAdapter : BaseAdapter<Model>() {

    override fun onDataArrived(models: MutableList<Model>) {
        data = models
        notifyDataSetChanged()
    }

    override fun onCreateViewModel(parent: ViewGroup?, viewType: Int): ViewModel<Model> {
        return when (Template.values()[viewType]) {
            Template.ITEM_VIDEO -> {
                VideoItemViewModel(parent, R.layout.item_video, Model())
                        .add(R.id.avatar, BaseViewManager())
                        .add(R.id.cover, BaseViewManager())
                        .add(R.id.artist, BaseViewManager())
                        .add(0, ActionViewManager(null))
            }
            else -> throw IllegalArgumentException("not support type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].template.ordinal
    }
}