package reed.flyingreed.lego

import reed.flyingreed.model.Model

/**
 * Created by thinkreed on 2017/8/4.
 */
interface DataProviderFactory {
    fun <T> getDataProvider():DataProvider<out Model>
}