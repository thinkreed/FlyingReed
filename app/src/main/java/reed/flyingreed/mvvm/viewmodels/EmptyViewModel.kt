package reed.flyingreed.mvvm.viewmodels

import reed.flyingreed.mvvm.ViewModel

/**
 * Created by thinkreed on 2017/6/19.
 */

object EmptyViewModel : ViewModel() {

    override fun notifyObserver() {
        //do nothing
    }
}