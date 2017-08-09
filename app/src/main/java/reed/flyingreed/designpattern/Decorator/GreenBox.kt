package reed.flyingreed.designpattern.Decorator

/**
 * Created by thinkreed on 2017/8/9.
 */
class GreenBox(val box: Box) : Box {
    override fun boxing() {
        box.boxing()
        println("green boxing")
    }
}