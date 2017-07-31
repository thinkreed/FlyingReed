package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_empty.*
import reed.flyingreed.R

/**
 * Created by thinkreed on 2017/7/3.
 */

class LiveFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_empty, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message.text = "live coming soon"
    }

    companion object {
        fun getInstance(): LiveFragment {
            return LiveFragment()
        }
    }
}