package reed.flyingreed.controller.activity

import android.support.v4.app.Fragment
import reed.flyingreed.controller.fragment.VideoPlayerFragment

/**
 * Created by thinkreed on 2017/7/6.
 */
class VideoPlayerActivity : DetectorActivity() {
    override fun getFragment(): Fragment {
        return VideoPlayerFragment.getInstance(intent.extras)
    }
}