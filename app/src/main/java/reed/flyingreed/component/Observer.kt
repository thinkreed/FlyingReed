package reed.flyingreed.component

import reed.flyingreed.model.Model

/**
 * Created by thinkreed on 2017/6/18.
 */

interface Observer {
    fun onDataArrived(models: MutableList<Model>):Unit
}