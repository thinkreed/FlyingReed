package reed.flyingreed.controller.legos

import android.view.View
import reed.flyingreed.lego.ViewGroupLego

/**
 * Created by thinkreed on 2017/8/3.
 */
class EmptyFragmentLego(parentView: View) : ViewGroupLego(parentView) {

    override fun installLego() {
        addLego(TextLego(mParentView))
    }
}