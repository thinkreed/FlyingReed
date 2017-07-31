package reed.flyingreed.mvvm.viewmanagers

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.MediaController
import android.widget.PopupWindow
import reed.flyingreed.R
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewManager
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
                            val contentView = LayoutInflater.from(v.context)
                                    .inflate(R.layout.fragment_video_player, null)
                            val pop = DragWindow(contentView,
                                    WindowManager.LayoutParams.MATCH_PARENT,
                                    WindowManager.LayoutParams.MATCH_PARENT, true)
                            pop.isTouchable = false
                            pop.showAsDropDown(v)
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