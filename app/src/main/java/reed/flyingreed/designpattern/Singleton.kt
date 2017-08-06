package reed.flyingreed.designpattern

/**
 * Created by thinkreed on 2017/8/6.
 */
class Singleton private constructor() {

    private object Holder {
        val INSTANCE = Singleton()
    }

    companion object {
        val instance by lazy {
            Holder.INSTANCE
        }
    }
}