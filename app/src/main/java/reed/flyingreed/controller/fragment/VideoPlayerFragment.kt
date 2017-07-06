package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reed.flyingreed.R

/**
 * Created by thinkreed on 2017/7/6.
 */
class VideoPlayerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun getInstance(bundle: Bundle): VideoPlayerFragment {
            val fragment = VideoPlayerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}