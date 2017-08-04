package reed.flyingreed.controller.legos

import reed.flyingreed.lego.DataProvider
import reed.flyingreed.model.Model

/**
 * Created by thinkreed on 2017/8/4.
 */
class SimpleDataProvider :DataProvider<Model> {
    override fun getData(): Model {
        return Model()
    }
}