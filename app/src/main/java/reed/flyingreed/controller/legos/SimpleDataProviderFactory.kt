package reed.flyingreed.controller.legos

import reed.flyingreed.lego.DataProvider
import reed.flyingreed.lego.DataProviderFactory
import reed.flyingreed.model.Model

/**
 * Created by thinkreed on 2017/8/4.
 */
class SimpleDataProviderFactory :DataProviderFactory {

    override fun <T> getDataProvider(): DataProvider<out Model> {
        return SimpleDataProvider()
    }
}