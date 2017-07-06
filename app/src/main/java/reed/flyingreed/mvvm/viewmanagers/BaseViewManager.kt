package reed.flyingreed.mvvm.viewmanagers

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import reed.flyingreed.R
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.ViewManager

/**
 * Created by thinkreed on 2017/6/17.
 */

class BaseViewManager : ViewManager<Model>() {

    override fun bind(old: Model, new: Model) {

        when (id) {

            R.id.artist -> {
                if (old.description != new.description) {
                    val tv = view as TextView
                    tv.text = new.description
                }
            }

            R.id.title -> {
                if (old.title != new.title) {
                    val tv = view as TextView
                    tv.text = new.title
                }
            }

            R.id.cover -> {
                if (old.cover != new.cover) {
                    val img = view as ImageView
                    Glide.with(view).load(new.cover)
                            .apply(RequestOptions().placeholder(R.drawable.ys))
                            .into(img)
                }
            }

            R.id.avatar -> {
                if (old.cover != new.cover) {
                    val img = view as ImageView
                    Glide.with(view).load(new.cover)
                            .apply(RequestOptions.circleCropTransform()).into(img)
                }
            }

            else -> throw IllegalArgumentException("not supported type")

        }
    }
}