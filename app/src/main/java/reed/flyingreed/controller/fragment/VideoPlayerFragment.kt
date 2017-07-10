package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_video_player.*
import kotlinx.android.synthetic.main.item_media_control.*
import reed.flyingreed.R
import reed.flyingreed.model.Const
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewModel
import reed.flyingreed.mvvm.viewmanagers.ActionViewManager
import reed.flyingreed.mvvm.viewmodels.VideoPlayerViewModel

/**
 * Created by thinkreed on 2017/7/6.
 */
class VideoPlayerFragment : Fragment() {

    private lateinit var mViewModel: ViewModel<Model>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewModel = VideoPlayerViewModel(container,
                R.layout.fragment_video_player, Model())
                .add(R.id.video_player, ActionViewManager(null))
        return mViewModel.rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.model = arguments.getParcelable(Const.KEY_MODEL)
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        fun getInstance(bundle: Bundle): VideoPlayerFragment {
            val fragment = VideoPlayerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}