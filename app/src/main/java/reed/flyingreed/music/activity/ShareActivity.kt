package reed.flyingreed.music.activity

import android.content.Intent
import android.support.v4.app.Fragment
import com.umeng.socialize.UMShareAPI
import reed.flyingreed.common.activity.DetectorActivity
import reed.flyingreed.music.ShareFragment

/**
 * Created by thinkreed on 2017/7/1.
 */

class ShareActivity : DetectorActivity() {

    override fun getFragment(): Fragment {
        return ShareFragment.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
}