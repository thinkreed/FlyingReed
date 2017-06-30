package reed.flyingreed.mvvm.viewmanagers

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import reed.flyingreed.model.Const
import reed.flyingreed.model.Model
import reed.flyingreed.model.Mood
import reed.flyingreed.mvvm.ViewManager

/**
 * Created by thinkreed on 2017/6/28.
 */

class CommandViewManager : ViewManager() {

    override fun bind(model: Model) {
        when (id) {
            0 -> {
                val v = view as ViewGroup
                v.setOnClickListener {
                    val intent = Intent(v.context, model.motivation.target)
                    val bundle = Bundle()
                    bundle.putInt(Const.KEY_MOOD, Mood.NORMAL.ordinal)
                    intent.putExtras(bundle)
                    v.context.startActivity(intent)
                }
            }
            else -> throw IllegalArgumentException("not support command")
        }
    }
}