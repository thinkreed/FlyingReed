package reed.flyingreed.common.presenter.Events

import kotlin.reflect.KClass

/**
 * Created by thinkreed on 2017/7/1.
 */
class FlingEvents(val direction:Int,val target:KClass<out Any>) {
    companion object {
        val LEFT = 0
        val RIGHT = 1
        val UP = 2
        val DOWN = 3
    }
}