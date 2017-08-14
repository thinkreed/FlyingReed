package reed.flyingreed.designpattern.strategy

/**
 * Created by thinkreed on 2017/8/14.
 */
class MinusStrategy : IStrategy {
    override fun compute(): Int {
        return 1 - 3
    }
}