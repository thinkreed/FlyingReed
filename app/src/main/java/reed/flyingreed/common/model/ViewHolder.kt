package reed.flyingreed.common.model

import android.support.v7.widget.RecyclerView
import reed.flyingreed.common.presenter.ViewGroupManager

/**
 * Created by thinkreed on 2017/6/17.
 */

class ViewHolder<T>(val viewGroupManager: ViewGroupManager<T>) : RecyclerView.ViewHolder(viewGroupManager.rootView)