package reed.flyingreed.common.component

import reed.flyingreed.common.model.Model

/**
 * Created by thinkreed on 2017/6/18.
 */

interface Observer {
    fun onDataArrived(models: MutableList<Model>):Unit
}