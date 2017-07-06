package reed.flyingreed.controller.activity

import android.support.v4.app.Fragment
import reed.flyingreed.controller.fragment.MusicPlayerFragment

/**
 * Created by thinkreed on 2017/6/28.
 */

class MusicPlayerActivity : DetectorActivity() {

    override fun getFragment(): Fragment {
        return MusicPlayerFragment.getInstance(intent.extras)
    }
}