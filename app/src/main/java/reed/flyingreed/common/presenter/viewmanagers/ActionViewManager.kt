package reed.flyingreed.common.presenter.viewmanagers

import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.MediaController
import reed.flyingreed.R
import reed.flyingreed.R.layout
import reed.flyingreed.common.model.Model
import reed.flyingreed.common.presenter.ViewManager
import reed.flyingreed.common.widget.IjkVideoView


/**
 * Created by thinkreed on 2017/7/7.
 */

class ActionViewManager(val action: ((Model) -> Unit)?) : ViewManager<Model>() {

  override fun bind(old: Model, new: Model) {
    if (action != null) {
      action.invoke(new)
      return
    }

    when (id) {
      VIEW_GROUP_ID -> {
        val v = view

        v?.let {
          v.setOnClickListener {
            showPlayerPage(v, new)
          }
        }

      }
      R.id.video_player -> {
        view?.let {
          startVideoPlay(new)
        }
      }
      else -> throw IllegalArgumentException("not support action")
    }

  }

  private fun startVideoPlay(new: Model) {
    val ijkVideoView = view as IjkVideoView
    setupMediaController(ijkVideoView)
    ijkVideoView.setVideoPath(new.video.path)
  }

  private fun setupMediaController(ijkVideoView: IjkVideoView) {
    val mediaController = MediaController(ijkVideoView.context)
    mediaController.setAnchorView(ijkVideoView)
  }

  private fun showPlayerPage(v: View, new: Model) {
    val contentView = LayoutInflater.from(v.context).inflate(layout.fragment_video_player, null)
    val pop = DragWindow(new, contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
        false)
    pop.isTouchable = true
    pop.isOutsideTouchable = true
    pop.showAsDropDown(v)
  }

  companion object {
    val VIEW_GROUP_ID = 0
  }
}