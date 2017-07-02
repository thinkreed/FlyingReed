package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_share.*
import reed.flyingreed.R

/**
 * Created by thinkreed on 2017/7/1.
 */

class ShareFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.let {
            wechat.setOnClickListener(this)
            friend.setOnClickListener(this)
            qq.setOnClickListener(this)
            weibo.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.wechat -> {

                }
                R.id.friend -> {

                }
                R.id.qq -> {

                }
                R.id.weibo -> {

                }
                else -> IllegalArgumentException("not supported share type")
            }
        }
    }

    companion object {
        fun getInstance(): ShareFragment {
            return ShareFragment()
        }
    }
}