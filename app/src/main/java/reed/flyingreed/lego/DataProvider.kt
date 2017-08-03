package reed.flyingreed.lego

/**
 * Created by thinkreed on 2017/8/3.
 *
 * Provide data to Lego.
 */
interface DataProvider<T> {

    /**
     * retrieve the data
     */
    fun getData(): T
}