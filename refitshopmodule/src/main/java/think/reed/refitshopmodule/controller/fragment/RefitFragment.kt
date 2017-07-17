package think.reed.refitshopmodule.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import think.reed.refitshopmodule.R

/**
 * Created by thinkreed on 2017/7/17.
 */
class RefitFragment :Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.refitshop_fragment_wrapper, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun getInstance(): RefitFragment {
            return RefitFragment()
        }
    }
}