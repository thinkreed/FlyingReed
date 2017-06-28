package reed.flyingreed.mvvm.viewmanagers

import android.view.View
import android.view.ViewGroup
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewManager
import java.lang.Compiler.command

/**
 * Created by thinkreed on 2017/6/28.
 */

class CommandViewManager:ViewManager() {

    override fun bind(model: Model) {
        when (id) {
            0 -> {
                val v = view as ViewGroup
                v.setOnClickListener {

                }
            }
            else -> throw IllegalArgumentException("not support command")
        }
    }
}