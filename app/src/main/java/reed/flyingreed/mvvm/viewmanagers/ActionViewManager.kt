package reed.flyingreed.mvvm.viewmanagers

import android.content.Intent
import android.os.Bundle
import reed.flyingreed.controller.activity.VideoPlayerActivity
import reed.flyingreed.model.Const
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewManager


/**
 * Created by thinkreed on 2017/7/7.
 */

class ActionViewManager :ViewManager<Model>() {
    override fun bind(old: Model, new: Model) {
        val v = view
        v?.let {
            v.setOnClickListener {
                val intent = Intent(v.context, VideoPlayerActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(Const.KEY_MODEL, new)
                intent.putExtras(bundle)
                v.context.startActivity(intent)
            }
        }
    }
}