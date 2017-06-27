package reed.flyingreed.mvvm

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reed.flyingreed.model.Model

/**
 * Created by thinkreed on 2017/6/17.
 */

class ViewGroupManager(parent: ViewGroup?, layout: Int) {

    val rootView: View by lazy {
        LayoutInflater.from(parent?.context).inflate(layout, parent, false)
    }
    private val children by lazy {
        SparseArray<ViewManager>()
    }

    fun bind(model: Model) {

        for (i in 0 until children.size()) {
            findChild(i).bind(model)
        }

    }

    fun findChild(index: Int): ViewManager {

        val viewId = children.keyAt(index)

        val childView = if (viewId == 0) rootView else {
            rootView.findViewById(viewId)
        }

        val child = children.valueAt(index)
        child.view = childView
        child.id = viewId

        return child

    }

    fun add(id: Int, viewManager: ViewManager): ViewGroupManager {
        children.put(id, viewManager)
        return this
    }

    fun remove(id: Int) {
        children.remove(id)
    }
}
