package reed.flyingreed.mvvm

import reed.flyingreed.component.Observer
import reed.flyingreed.model.Model


/**
 * Created by thinkreed on 2017/6/19.
 */

abstract class ViewModel : Observer {

    private val data by lazy { mutableListOf<Model>() }

    override fun onDataArrived(models: MutableList<Model>) {
        data.clear()
        data.addAll(models)
        notifyObserver()
    }

    fun dataCount(): Int {
        return data.size
    }

    fun remove(index: Int) {
        data.removeAt(index)
    }

    fun remove(model: Model) {
        data.remove(model)
    }

    fun add(index: Int, model: Model) {
        data.add(index, model)
    }

    fun add(model: Model) {
        data.add(model)
    }

    fun get(index: Int): Model {
        return data[index]
    }

    fun clear() {
        data.clear()
    }

    abstract fun notifyObserver(): Unit
}
