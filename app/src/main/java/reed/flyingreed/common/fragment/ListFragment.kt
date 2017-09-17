package reed.flyingreed.common.fragment

import android.os.Bundle
import android.provider.MediaStore.Video.Media
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.list
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import reed.flyingreed.R
import reed.flyingreed.common.adapter.ListAdapter
import reed.flyingreed.common.component.DataFetcher

/**
 * Created by thinkreed on 2017/6/17.
 */

class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      setupLinearListAdapter()
      asyncFetchItems()
    }

  private fun setupLinearListAdapter() {
    val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    list.layoutManager = layoutManager
    val adapter = ListAdapter()
    list.adapter = adapter
  }

  private fun asyncFetchItems() {
    launch(CommonPool) {
      DataFetcher.getData(Media.EXTERNAL_CONTENT_URI, DataFetcher::getVideos)
    }
  }

  override fun onDestroyView() {
        //reset adapter
        list.adapter = null
        super.onDestroyView()
    }

    companion object {
        val instance: ListFragment
            get() = ListFragment()
    }
}