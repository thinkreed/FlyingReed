package reed.flyingreed.designpattern.bridge

/**
 * Created by thinkreed on 2017/8/10.
 */
class SandyTire : ITire {
    override fun run() {
        println("run on sandy road")
    }
}