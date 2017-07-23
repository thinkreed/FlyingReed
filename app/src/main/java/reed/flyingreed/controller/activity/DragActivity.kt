package reed.flyingreed.controller.activity

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import reed.flyingreed.widget.DragLayout

/**
 * Created by thinkreed on 2017/7/23.
 */
abstract class DragActivity : FragmentActivity() {

    private lateinit var mDragLayout: DragLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDragLayout = DragLayout(this)
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(mDragLayout)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mDragLayout.setDragContent(LayoutInflater.from(this).inflate(layoutResID, null),
                { top, _ ->
                    val lp = window.attributes
                    lp.y = top
                    window.attributes = lp
                })
    }
}