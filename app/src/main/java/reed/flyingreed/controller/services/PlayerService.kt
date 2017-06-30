package reed.flyingreed.controller.services

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import android.provider.MediaStore
import org.greenrobot.eventbus.EventBus
import reed.flyingreed.IPlayerService
import reed.flyingreed.component.DataFetcher
import reed.flyingreed.component.Observer
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.Events.MusicChangeEvent
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/**
 * Created by thinkreed on 2017/6/30.
 */

class PlayerService : Service(), Observer {

    private val mPlayer by lazy {
        IjkMediaPlayer()
    }

    private var mMusicIndex = 0

    private lateinit var mData: MutableList<Model>

    private val mBinder = object : IPlayerService.Stub() {

        override fun pause() {
            mPlayer.pause()
        }

        override fun start() {
            mPlayer.start()
        }

        override fun stop() {
            mPlayer.stop()
        }

        override fun prev() {
            mMusicIndex = if (mMusicIndex == 0) mData.size - 1 else mMusicIndex - 1
            startPlayAtIndex(mMusicIndex)
        }

        override fun next() {
            mMusicIndex = if (mMusicIndex == mMusicIndex - 1) 0 else mMusicIndex + 1
            startPlayAtIndex(mMusicIndex)
        }

        override fun getCurrentPosition(): Long {
            return mPlayer.currentPosition
        }

        override fun getDuration(): Long {
            return mPlayer.duration
        }

    }


    override fun onCreate() {
        super.onCreate()
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        DataFetcher.registerObserver(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        mPlayer.setOnPreparedListener {
            mPlayer.start()
            EventBus.getDefault().post(MusicChangeEvent(mData[mMusicIndex]))
        }
        mPlayer.setOnCompletionListener {
            mBinder.next()
        }
        DataFetcher.getData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        return mBinder
    }

    override fun onDestroy() {
        DataFetcher.unregisterObserver(this)
        super.onDestroy()
    }

    override fun onDataArrived(models: MutableList<Model>) {
        mData = models
        if (mData.isNotEmpty()) {
            startPlayAtIndex(0)
        }
    }

    private fun startPlayAtIndex(index: Int) {
        val model = mData[index]
        mPlayer.stop()
        mPlayer.reset()
        mPlayer.dataSource = model.music.path
        mPlayer.prepareAsync()
    }
}