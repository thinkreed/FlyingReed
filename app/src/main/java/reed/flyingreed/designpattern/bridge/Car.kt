package reed.flyingreed.designpattern.bridge

/**
 * Created by thinkreed on 2017/8/10.
 */
abstract class Car(val tire: ITire) {

    abstract fun run()
}