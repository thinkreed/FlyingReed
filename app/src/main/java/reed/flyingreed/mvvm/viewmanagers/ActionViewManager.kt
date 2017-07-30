package reed.flyingreed.mvvm.viewmanagers

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.MediaController
import reed.flyingreed.R
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewManager
import reed.flyingreed.widget.DragLayout
import reed.flyingreed.widget.DragWindow
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
                    val v = view
                    v?.let {
                        v.setOnClickListener {
                            val window = DragWindow(v.context, R.layout.fragment_video_player)
                            window.show(v.context)
                        }
                    }

                }
                R.id.video_player -> {
                    view?.let {
                        val ijkVideoView = view as IjkVideoView
                        val mediaController = MediaController(ijkVideoView.context)
                        mediaController.setAnchorView(ijkVideoView)
                        ijkVideoView.setMediaController(mediaController)
                        ijkVideoView.setVideoPath(new.video.path)
                    }
                }
                else -> throw IllegalArgumentException("not support action")
            }
        }

    }
}