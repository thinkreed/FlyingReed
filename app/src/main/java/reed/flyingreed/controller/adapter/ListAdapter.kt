package reed.flyingreed.controller.adapter

import android.view.ViewGroup
import reed.flyingreed.R
import reed.flyingreed.model.Model
import reed.flyingreed.model.Template
import reed.flyingreed.mvvm.ViewModel
import reed.flyingreed.mvvm.viewmanagers.BaseViewManager
import reed.flyingreed.mvvm.viewmodels.VideoItemViewModel

/**
 * Created by thinkreed on 2017/7/4.
 */

class ListAdapter : BaseAdapter<Model>() {

    override fun onDataArrived(models: MutableList<Model>) {
        data = models
    }

    override fun onCreateViewModel(parent: ViewGroup?, viewType: Int): ViewModel<Model> {
        return when (Template.values()[viewType]) {
            Template.ITEM_VIDEO -> {
                VideoItemViewModel(ViewGroupManager<Model>(parent, R.layout.item_video)
                        .add(R.id.avatar, BaseViewManager()))
            }
            else -> throw IllegalArgumentException("not support type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].template.ordinal
    }
}