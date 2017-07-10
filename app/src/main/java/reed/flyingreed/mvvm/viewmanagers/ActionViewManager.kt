package reed.flyingreed.mvvm.viewmanagers

import android.content.Intent
import android.os.Bundle
import reed.flyingreed.R
import reed.flyingreed.controller.activity.VideoPlayerActivity
import reed.flyingreed.model.Const
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewManager
import reed.flyingreed.widget.IjkVideoView


/**
 * Created by thinkreed on 2017/7/7.
 */

class ActionViewManager(val action: ((Model) -> Unit)?) : ViewManager<Model>() {
    override fun bind(old: Model, new: Model) {
        if (action != null) {
            action.invoke(new)
        } else {
            when (id) {
                0 -> {
                    view?.let {
                        val v = view
                        val intent = Intent(v?.context, VideoPlayerActivity::class.java)
                        val bundle = Bundle()
                        bundle.putParcelable(Const.KEY_MODEL, new)
                        intent.putExtras(bundle)
                        view?.context?.startActivity(intent)
                    }
                }
                R.id.video_player -> {
                    view.let {
                        val ijkVideoView = view as IjkVideoView
                        ijkVideoView.setVideoPath(new.video.path)
                    }
                }
                else -> throw IllegalArgumentException("not support action")
            }
        }

    }
}