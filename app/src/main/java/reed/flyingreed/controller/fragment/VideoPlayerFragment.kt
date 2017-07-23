package reed.flyingreed.controller.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_video_player.*
import kotlinx.android.synthetic.main.item_media_control.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import reed.flyingreed.R
import reed.flyingreed.controller.activity.MusicPlayerActivity
import reed.flyingreed.controller.activity.ShareActivity
import reed.flyingreed.controller.activity.VideoPlayerActivity
import reed.flyingreed.model.Const
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.Events.FlingEvents
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
        EventBus.getDefault().register(this)
        mViewModel.model = arguments.getParcelable(Const.KEY_MODEL)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFlingEvent(event: FlingEvents) {
        if (event.target == VideoPlayerActivity::class) {

            when (event.direction) {
                FlingEvents.LEFT -> {
                    val x = video_player.width / 4
                    val y = activity.window.decorView.height - video_player.height
                    video_player.animate()
                            .scaleX(0.5f)
                            .scaleY(0.5f)
                            .x(x.toFloat())
                            .y(y.toFloat())
                }
                FlingEvents.RIGHT -> {
                    video_player.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .x(0f)
                            .y(0f)
                }
                FlingEvents.UP -> {
                    val intent = Intent(activity, ShareActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    companion object {
        fun getInstance(bundle: Bundle): VideoPlayerFragment {
            val fragment = VideoPlayerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}