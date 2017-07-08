package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_video_player.*
import reed.flyingreed.R
import reed.flyingreed.model.Const
import reed.flyingreed.model.Model

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

    override fun onResume() {
        super.onResume()
        val model: Model = arguments.getParcelable(Const.KEY_MODEL)
        video_player.setVideoPath(model.video.path)
    }

    companion object {
        fun getInstance(bundle: Bundle): VideoPlayerFragment {
            val fragment = VideoPlayerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}