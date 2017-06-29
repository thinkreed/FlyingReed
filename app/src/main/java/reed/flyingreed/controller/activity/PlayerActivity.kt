package reed.flyingreed.controller.activity

import android.support.v4.app.Fragment
import reed.flyingreed.controller.fragment.PlayerFragment

/**
 * Created by thinkreed on 2017/6/28.
 */

class PlayerActivity : FragmentActivity() {

    override fun getFragment(): Fragment {
        return PlayerFragment.getInstance(intent)
    }
}