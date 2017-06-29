package reed.flyingreed.controller.fragment

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reed.flyingreed.R
import reed.flyingreed.model.Model
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import kotlinx.android.synthetic.main.fragment_player.*;
import reed.flyingreed.R.id.progress
import reed.flyingreed.widget.PlayPauseProgress

/**
 * Created by thinkreed on 2017/6/28.
 */

class PlayerFragment : Fragment(), PlayPauseProgress.OnStateChangeListener {

    private val mPlayer by lazy {
        IjkMediaPlayer()
    }

    private val mHandler by lazy {
        Handler()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val model = arguments.getParcelable<Model>("model")
            progress.setOnStateChangeListener(this)
            mPlayer.setOnCompletionListener {
                mHandler.removeCallbacksAndMessages(null)
                progress.setProgress(100f)
            }
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mPlayer.dataSource = model.music.path
            mPlayer.setOnPreparedListener {
                mPlayer.start()
                mHandler.post(UpdatingTask())
            }
            mPlayer.prepareAsync()
        }
    }

    override fun onDetach() {
        mHandler.removeCallbacksAndMessages(null)
        mPlayer.stop()
        mPlayer.release()
        super.onDetach()
    }

    override fun onStateChanged(state: PlayPauseProgress.State) {
        when (state) {
            PlayPauseProgress.State.IDLE -> {
                mHandler.removeCallbacksAndMessages(null)
                mPlayer.pause()
            }
            PlayPauseProgress.State.UPDATING -> {
                mPlayer.start()
                mHandler.post(UpdatingTask())
            }
            else -> return
        }
    }


    companion object {
        fun getInstance(intent: Intent): Fragment {
            val fragment = PlayerFragment()
            fragment.arguments = intent.extras
            return fragment
        }
    }

    inner class UpdatingTask : Runnable {
        override fun run() {
            progress.setProgress(mPlayer.currentPosition.toFloat() / mPlayer.duration.toFloat())
            mHandler.postDelayed(this, 1000)
        }
    }

}