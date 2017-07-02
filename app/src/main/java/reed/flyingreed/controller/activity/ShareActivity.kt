package reed.flyingreed.controller.activity

import android.support.v4.app.Fragment
import reed.flyingreed.controller.fragment.ShareFragment

/**
 * Created by thinkreed on 2017/7/1.
 */

class ShareActivity :DetectorActivity() {

    override fun getFragment(): Fragment {
        return ShareFragment.getInstance()
    }
}