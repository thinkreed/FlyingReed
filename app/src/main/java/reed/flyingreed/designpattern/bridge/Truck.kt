package reed.flyingreed.designpattern.bridge

/**
 * Created by thinkreed on 2017/8/10.
 */
class Truck(tire: ITire) : Car(tire) {
    override fun run() {
        tire.run()
    }
}