package reed.flyingreed.mvvm.viewmodels

import reed.flyingreed.controller.adapter.ListAdapter
import reed.flyingreed.mvvm.ViewModel

/**
 * Created by thinkreed on 2017/6/19.
 */

class SongsViewModel(val adapter: ListAdapter) : ViewModel() {

    override fun notifyObserver() {
        adapter.notifyDataSetChanged()
    }
}