package reed.flyingreed.designpattern.Decorator

/**
 * Created by thinkreed on 2017/8/9.
 */
class EmptyBox :Box {
    override fun boxing() {
        println("empty boxing")
    }
}