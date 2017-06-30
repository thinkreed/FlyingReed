package reed.flyingreed.controller.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_music.*
import reed.flyingreed.R
import reed.flyingreed.controller.activity.PlayerActivity
import reed.flyingreed.model.Const

/**
 * Created by thinkreed on 2017/6/30.
 */

class MusicFragment : Fragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.let {
            monday.setOnClickListener(this)
            tuesday.setOnClickListener(this)
            wednesday.setOnClickListener(this)
            thursday.setOnClickListener(this)
            friday.setOnClickListener(this)
            saturday.setOnClickListener(this)
            sunday.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            var weekColor = 0
            when (v.id) {
                R.id.monday -> {
                    weekColor = R.color.colorRed
                }
                R.id.tuesday -> {
                    weekColor = R.color.colorOrange

                }
                R.id.wednesday -> {
                    weekColor = R.color.colorYellow

                }
                R.id.thursday -> {
                    weekColor = R.color.colorSpringGreen

                }
                R.id.friday -> {
                    weekColor = R.color.colorLightSkyBlue

                }
                R.id.saturday -> {
                    weekColor = R.color.colorCyan

                }
                R.id.sunday -> {
                    weekColor = R.color.colorMediumPurple

                }
                else -> IllegalArgumentException("not a weekday")
            }
            val intent = Intent(this.context, PlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(Const.KEY_WEEK_COLOR, weekColor)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun getInstance(): MusicFragment {
            return MusicFragment()
        }
    }
}