package reed.flyingreed.controller.legos

import android.view.View
import android.widget.TextView
import reed.flyingreed.R
import reed.flyingreed.lego.BaseLego

/**
 * Created by thinkreed on 2017/8/3.
 */
class TextLego(parentView: View) : BaseLego(parentView) {

    private lateinit var mEmptyText: TextView

    override fun onInit() {
        mEmptyText = mParentView.findViewById(R.id.message) as TextView
    }

    override fun onRender() {
        mEmptyText.text = "this is in text lego"
    }
}