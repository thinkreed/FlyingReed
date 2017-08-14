package reed.flyingreed.designpattern.strategy

/**
 * Created by thinkreed on 2017/8/14.
 */
class Computer {

    private lateinit var mStrategy:IStrategy

    fun setStrategy(strategy: IStrategy) {
        mStrategy = strategy
    }

    fun doCompute() {
        //do something

        val result = mStrategy.compute()
    }
}