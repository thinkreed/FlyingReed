package reed.flyingreed.common.presenter.viewmodels

import android.view.ViewGroup
import reed.flyingreed.common.model.Model
import reed.flyingreed.common.presenter.ViewGroupManager

/**
 * Created by thinkreed on 2017/6/19.
 */

class VideoItemViewGroupManager(parent: ViewGroup?, layout: Int, model: Model)
    : ViewGroupManager<Model>(parent, layout, model)