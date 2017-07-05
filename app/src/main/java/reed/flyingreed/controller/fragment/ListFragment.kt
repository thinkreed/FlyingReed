package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reed.flyingreed.R
import reed.flyingreed.component.DataFetcher
import reed.flyingreed.controller.adapter.BaseAdapter
import reed.flyingreed.mvvm.viewmodels.VideoItemViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import reed.flyingreed.controller.adapter.ListAdapter

/**
 * Created by thinkreed on 2017/6/17.
 */

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        list.layoutManager = layoutManager
        val adapter = ListAdapter()
        list.adapter = adapter
        launch(CommonPool) {
            DataFetcher.getData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        val instance: ListFragment
            get() = ListFragment()
    }
}