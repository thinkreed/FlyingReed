package reed.flyingreed.common.presenter

import android.view.View

/**
 * Created by thinkreed on 2017/6/17.
 */

abstract class ViewManager<T>(var id: Int = Int.MIN_VALUE, var view: View? = null) {

    abstract fun bind(old: T, new:T)

}
