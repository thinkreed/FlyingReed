package reed.flyingreed.controller.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import reed.flyingreed.R
import reed.flyingreed.controller.fragment.VideoPlayerFragment

/**
 * Created by thinkreed on 2017/7/6.
 */
class VideoPlayerActivity : DragActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_single_fragment)

        var fragment = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment == null) {
            fragment = VideoPlayerFragment.getInstance(intent.extras)
            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
        }
    }
}