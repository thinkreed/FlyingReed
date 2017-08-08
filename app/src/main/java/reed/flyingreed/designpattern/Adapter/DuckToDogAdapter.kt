package reed.flyingreed.designpattern.Adapter

/**
 * Created by thinkreed on 2017/8/8.
 */
class DuckToDogAdapter(private val duck: Duck) : Dog {

    override fun bark() {
        duck.quack()
    }
}