package reed.flyingreed.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
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
                    ShareAction(activity).setPlatform(SHARE_MEDIA.QQ)
                            .withText("from flyingreed")
                            .withMedia(UMImage(activity, R.mipmap.ic_launcher))
                            .setCallback(object : UMShareListener {
                                override fun onStart(p0: SHARE_MEDIA?) {
                                }

                                override fun onResult(p0: SHARE_MEDIA?) {
                                    activity.onBackPressed()
                                }

                                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                                }

                                override fun onCancel(p0: SHARE_MEDIA?) {
                                }
                            })
                            .share()
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