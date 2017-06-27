package reed.flyingreed.mvvm.viewmanagers

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import reed.flyingreed.R
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewManager

/**
 * Created by thinkreed on 2017/6/17.
 */

class BaseViewManager : ViewManager() {

    override fun bind(model: Model) {

        when (id) {

            R.id.title -> {
                val tv = view as TextView
                tv.text = model.title
            }

            R.id.description -> {
                val tv = view as TextView
                tv.text = model.description
            }

            R.id.cover -> {
                val img = view as ImageView
                Glide.with(view).load(model.cover).into(img)
            }

            else -> throw IllegalArgumentException("not supported type")

        }
    }
}