package reed.flyingreed.designpattern.observer

/**
 * Created by thinkreed on 2017/8/7.
 */
interface Observable {
    
    fun registerObserver(observer: Observer)

    fun unRegisterObserver(observer: Observer)
}