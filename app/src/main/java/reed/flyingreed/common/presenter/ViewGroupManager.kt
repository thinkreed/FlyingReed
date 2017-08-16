package reed.flyingreed.common.presenter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.properties.Delegates

/**
 * Created by thinkreed on 2017/6/19.
 */

abstract class ViewGroupManager<T>(parent: ViewGroup?, layout: Int, defaultValue: T) {

    val rootView: View by lazy {
        LayoutInflater.from(parent?.context).inflate(layout, parent, false)
    }

    private val children by lazy {
        SparseArray<ViewManager<T>>()
    }

    var model: T by Delegates.observable(defaultValue) {
        prop, old, new ->
        bind(old, new)
    }

    fun bind(old: T, new: T) {

        for (i in 0 until children.size()) {
            findChild(i).bind(old, new)
        }

    }

    fun findChild(index: Int): ViewManager<T> {

        val viewId = children.keyAt(index)
        val child = children.valueAt(index)

        if (child.view == null) {
            child.view = if (viewId == 0) rootView else {
                rootView.findViewById(viewId)
            }
            child.id = viewId
        }

        return child

    }

    fun add(id: Int, viewManager: ViewManager<T>): ViewGroupManager<T> {
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
