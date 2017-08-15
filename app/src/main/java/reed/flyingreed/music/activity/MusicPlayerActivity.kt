package reed.flyingreed.music.activity

import android.support.v4.app.Fragment
import reed.flyingreed.common.activity.DetectorActivity
import reed.flyingreed.music.MusicPlayerFragment

/**
 * Created by thinkreed on 2017/6/28.
 */

class MusicPlayerActivity : DetectorActivity() {

    override fun getFragment(): Fragment {
        return MusicPlayerFragment.getInstance(intent.extras)
    }
}