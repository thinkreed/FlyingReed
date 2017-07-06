package reed.flyingreed.controller.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reed.flyingreed.R
import kotlinx.android.synthetic.main.fragment_player.*;
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import reed.flyingreed.IPlayerService
import reed.flyingreed.controller.activity.MusicPlayerActivity
import reed.flyingreed.controller.activity.ShareActivity
import reed.flyingreed.controller.services.PlayerService
import reed.flyingreed.model.Const
import reed.flyingreed.model.Week
import reed.flyingreed.mvvm.Events.FlingEvents
import reed.flyingreed.mvvm.Events.MediaChangeEvent
import reed.flyingreed.widget.PlayPauseProgress

/**
 * Created by thinkreed on 2017/6/28.
 */

class MusicPlayerFragment : Fragment(), PlayPauseProgress.OnStateChangeListener {


    private val mHandler by lazy {
        Handler()
    }

    private var mFavor = 0

    private var mWidgetColor = 0

    private var mRoot: View? = null

    private val mServiceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mPlayerService = IPlayerService.Stub.asInterface(service)
                if (mPlayerService.favor != mFavor) {
                    mPlayerService.initWithFavor(mFavor)
                } else {
                    title.text = mPlayerService.currentPlaying.title
                    artist.text = mPlayerService.currentPlaying.description
                    if (mPlayerService.isPlaying) {
                        mHandler.post(UpdatingTask())
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                mHandler.removeCallbacksAndMessages(null)
            }
        }
    }

    private lateinit var mPlayerService: IPlayerService

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_music_player, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        arguments?.let {
            mRoot = view
            mFavor = arguments.getInt(Const.KEY_WEEK, Week.FRIDAY.ordinal)
            val intent = Intent(activity, PlayerService::class.java)
            activity.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
            when (Week.values()[mFavor]) {
                Week.MONDAY -> {
                    mWidgetColor = R.color.colorRed
                }
                Week.TUESDAY -> {
                    mWidgetColor = R.color.colorOrange
                }
                Week.WEDNESDAY -> {
                    mWidgetColor = R.color.colorYellow
                }
                Week.THURSDAY -> {
                    mWidgetColor = R.color.colorSpringGreen
                }
                Week.FRIDAY -> {
                    mWidgetColor = R.color.colorLightSkyBlue
                }
            }
            progress.setOnStateChangeListener(this)
            progress.setThemeColor(mWidgetColor)
            title.setTextColor(resources.getColor(mWidgetColor))
            artist.setTextColor(resources.getColor(mWidgetColor))
        }
    }

    override fun onStop() {
        mHandler.removeCallbacksAndMessages(null)
        super.onStop()
    }

    override fun onDetach() {
        EventBus.getDefault().unregister(this)
        activity.unbindService(mServiceConnection)
        super.onDetach()
    }

    override fun onStateChanged(state: PlayPauseProgress.State) {
        when (state) {
            PlayPauseProgress.State.IDLE -> {
                mHandler.removeCallbacksAndMessages(null)
                mPlayerService.pause()
                title.setTextColor(resources.getColor(R.color.colorWhite))
                artist.setTextColor(resources.getColor(R.color.colorWhite))
                mRoot?.setBackgroundColor(resources.getColor(mWidgetColor))
            }
            PlayPauseProgress.State.UPDATING -> {
                mPlayerService.start()
                mHandler.post(UpdatingTask())
                title.setTextColor(resources.getColor(mWidgetColor))
                artist.setTextColor(resources.getColor(mWidgetColor))
                mRoot?.setBackgroundColor(resources.getColor(R.color.colorGray))
            }
            else -> return
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMusicChangeEvent(event: MediaChangeEvent) {
        title.text = event.model.title
        artist.text = event.model.description
        mHandler.removeCallbacksAndMessages(null)
        mHandler.post(UpdatingTask())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFlingEvent(event: FlingEvents) {
        if (event.target == MusicPlayerActivity::class) {

            when (event.direction) {
                FlingEvents.LEFT -> {
                    mPlayerService.next()
                }
                FlingEvents.RIGHT -> {
                    mPlayerService.prev()
                }
                FlingEvents.UP -> {
                    val intent = Intent(activity, ShareActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    companion object {
        fun getInstance(bundle: Bundle): Fragment {
            val fragment = MusicPlayerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    inner class UpdatingTask : Runnable {
        override fun run() {
            progress.setProgress(mPlayerService.currentPosition.toFloat() / mPlayerService.duration.toFloat())
            mHandler.postDelayed(this, 1000)
        }
    }

}