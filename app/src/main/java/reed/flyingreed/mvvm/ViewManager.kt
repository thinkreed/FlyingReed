package reed.flyingreed.mvvm

import android.view.View
import reed.flyingreed.model.Model

/**
 * Created by thinkreed on 2017/6/17.
 */

abstract class ViewManager<T>(var id: Int = Int.MIN_VALUE, var view: View? = null) {

    abstract fun bind(model: T, viewModel: ViewModel<T>)

}
