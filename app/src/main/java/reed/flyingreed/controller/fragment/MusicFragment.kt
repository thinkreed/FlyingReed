package reed.flyingreed.controller.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_music.*
import reed.flyingreed.IPlayerService
import reed.flyingreed.R
import reed.flyingreed.controller.activity.PlayerActivity
import reed.flyingreed.controller.services.PlayerService
import reed.flyingreed.model.Const
import reed.flyingreed.model.Week

/**
 * Created by thinkreed on 2017/6/30.
 */

class MusicFragment : Fragment(), View.OnClickListener {

    private var mPlayerService: IPlayerService? = null

    private val mHandler by lazy {
        Handler()
    }

    private val mServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mPlayerService = IPlayerService.Stub.asInterface(service)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }
    }


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
            val intent = Intent(activity, PlayerService::class.java)
            activity.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            var week = 0
            when (v.id) {
                R.id.monday -> {
                    week = Week.MONDAY.ordinal
                }
                R.id.tuesday -> {
                    week = Week.TUESDAY.ordinal

                }
                R.id.wednesday -> {
                    week = Week.WEDNESDAY.ordinal

                }
                R.id.thursday -> {
                    week = Week.THURSDAY.ordinal

                }
                R.id.friday -> {
                    week = Week.FRIDAY.ordinal

                }
                R.id.saturday -> {
                    week = Week.SATURDAY.ordinal

                }
                R.id.sunday -> {
                    week = Week.SUNDAY.ordinal

                }
                else -> IllegalArgumentException("not a weekday")
            }
            val intent = Intent(this.context, PlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(Const.KEY_WEEK, week)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val service = mPlayerService
        if (service != null) {
            resetAlpha()
            if (service.isPlaying) {
                val view = when (Week.values()[service.favor]) {
                    Week.MONDAY -> {
                        monday
                    }
                    Week.TUESDAY -> {
                        tuesday
                    }
                    Week.WEDNESDAY -> {
                        wednesday
                    }
                    Week.THURSDAY -> {
                        thursday
                    }
                    Week.FRIDAY -> {
                        friday
                    }
                    Week.SATURDAY -> {
                        saturday
                    }
                    else -> {
                        sunday
                    }
                }
                mHandler.post(ShiningTask(view))
            }
        }

    }

    override fun onStop() {
        mHandler.removeCallbacksAndMessages(null)
        super.onStop()
    }

    override fun onDestroyView() {
        activity.unbindService(mServiceConnection)
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun resetAlpha() {
        monday.alpha = 1.0f
        tuesday.alpha = 1.0f
        wednesday.alpha = 1.0f
        thursday.alpha = 1.0f
        friday.alpha = 1.0f
        saturday.alpha = 1.0f
        sunday.alpha = 1.0f
    }

    inner class ShiningTask(var view: View) : Runnable {
        override fun run() {
            val alpha = if (view.alpha == 0f) 1f else 0f
            view.animate().alpha(alpha).duration = 1000
            //设置时间比duration稍长使得新的alpha不会cancel上一个animation
            mHandler.postDelayed(this, 1100)
        }
    }

    companion object {
        fun getInstance(): MusicFragment {
            return MusicFragment()
        }
    }
}