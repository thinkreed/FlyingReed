package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reed.flyingreed.R
import reed.flyingreed.component.DataFetcher
import reed.flyingreed.controller.adapter.ListAdapter
import reed.flyingreed.mvvm.viewmodels.SongsViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

/**
 * Created by thinkreed on 2017/6/17.
 */

class ListFragment : Fragment() {

    private lateinit var songs:SongsViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(activity)
        val adapter = ListAdapter()
        songs = SongsViewModel(adapter)
        DataFetcher.registerObserver(songs)
        adapter.setViewModel(songs)
        list.adapter = adapter
        launch(CommonPool) {
            DataFetcher.getData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        }
    }

    override fun onDestroyView() {
        DataFetcher.unregisterObserver(songs)
        super.onDestroyView()
    }

    companion object {
        val instance: ListFragment
            get() = ListFragment()
    }
}