package reed.flyingreed.designpattern.observer

import kotlin.properties.Delegates

/**
 * Created by thinkreed on 2017/8/7.
 */
class SimpleObservable : Observable {

    private var mData by Delegates.observable(0) {
        _, _, new ->
        mObservers.map { observer -> observer.onDataSetChanged(new) }
    }

    private val mObservers by lazy {
        mutableListOf<Observer>()
    }

    override fun registerObserver(observer: Observer) {
        mObservers.add(observer)
    }

    override fun unRegisterObserver(observer: Observer) {
        mObservers.remove(observer)
    }
}