package reed.flyingreed.mvvm

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by thinkreed on 2017/6/19.
 */

abstract class ViewModel<T>(parent: ViewGroup?, layout: Int) {

    val rootView: View by lazy {
        LayoutInflater.from(parent?.context).inflate(layout, parent, false)
    }

    private val children by lazy {
        SparseArray<ViewManager<T>>()
    }

    fun bind(model: T) {

        for (i in 0 until children.size()) {
            findChild(i).bind(model, this)
        }

    }

    fun findChild(index: Int): ViewManager<T> {

        val viewId = children.keyAt(index)

        val childView = if (viewId == 0) rootView else {
            rootView.findViewById(viewId)
        }

        val child = children.valueAt(index)
        child.view = childView
        child.id = viewId

        return child

    }

    fun add(id: Int, viewManager: ViewManager<T>): ViewModel<T> {
        children.put(id, viewManager)
        return this
    }

    fun remove(id: Int) {
        children.remove(id)
    }

    fun start() {

    }

    fun stop() {

    }

}
