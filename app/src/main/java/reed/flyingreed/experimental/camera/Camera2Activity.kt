package reed.flyingreed.experimental.camera

import android.support.v4.app.Fragment
import reed.flyingreed.controller.activity.FragmentActivity

/**
 * Created by thinkreed on 2017/7/27.
 */
class Camera2Activity : FragmentActivity() {
    override fun getFragment(): Fragment {
        return Camera2Fragment.getInstance()
    }
}