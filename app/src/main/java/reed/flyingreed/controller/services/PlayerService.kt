package reed.flyingreed.controller.services

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import android.provider.MediaStore
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import reed.flyingreed.IPlayerService
import reed.flyingreed.component.DataFetcher
import reed.flyingreed.component.Observer
import reed.flyingreed.model.Model
import reed.flyingreed.mvvm.Events.MediaChangeEvent
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/**
 * Created by thinkreed on 2017/6/30.
 */

class PlayerService : Service(), Observer {

    private val mPlayer by lazy {
        IjkMediaPlayer()
    }

    private var mFavor = -1

    private var mMusicIndex = -1

    private lateinit var mData: MutableList<Model>

    private var mJob: Job? = null

    private val mBinder = object : IPlayerService.Stub() {

        override fun pause() {
            mPlayer.pause()
        }

        override fun start() {
            if (mData.isNotEmpty()) {
                mPlayer.start()
                EventBus.getDefault().post(MediaChangeEvent(mData[mMusicIndex]))
            }
        }

        override fun stop() {
            mPlayer.stop()
        }

        override fun prev() {
            mMusicIndex = if (mMusicIndex == 0) mData.size - 1 else mMusicIndex - 1
            startPlayAtIndex(mMusicIndex)
        }

        override fun next() {
            mMusicIndex = if (mMusicIndex == mData.size - 1) 0 else mMusicIndex + 1
            startPlayAtIndex(mMusicIndex)
        }

        override fun getCurrentPosition(): Long {
            return mPlayer.currentPosition
        }

        override fun getDuration(): Long {
            return mPlayer.duration
        }

        override fun initWithFavor(favor: Int) {
            mFavor = favor
            mJob = launch(CommonPool) {
                DataFetcher.getData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        DataFetcher::getMusics)
            }
        }

        override fun isPlaying(): Boolean {
            return mPlayer.isPlaying
        }

        override fun getFavor(): Int {
            return mFavor
        }

        override fun getCurrentPlaying(): Model {
            return mData[mMusicIndex]
        }

    }


    override fun onCreate() {
        super.onCreate()
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mPlayer.setOnPreparedListener {
            mBinder.start()
        }
        mPlayer.setOnCompletionListener {
            mBinder.next()
        }
        DataFetcher.registerObserver(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mJob?.cancel()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        DataFetcher.unregisterObserver(this)
        mPlayer.stop()
        mPlayer.release()
        super.onDestroy()
    }

    override fun onDataArrived(models: MutableList<Model>) {
        mData = models
        if (mData.isNotEmpty()) {
            startPlayAtIndex(mFavor * 3 % mData.size)
        }
    }

    private fun startPlayAtIndex(index: Int) {
        mMusicIndex = index
        val model = mData[index]
        mPlayer.stop()
        mPlayer.reset()
        mPlayer.dataSource = model.music.path
        mPlayer.prepareAsync()
    }
}