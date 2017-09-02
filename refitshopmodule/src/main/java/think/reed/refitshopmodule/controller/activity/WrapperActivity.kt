package think.reed.refitshopmodule.controller.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.refitshop_fragment_wrapper.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import think.reed.refitshopmodule.R
import think.reed.refitshopmodule.mediacodec.BufferReadyEvent
import think.reed.refitshopmodule.mediacodec.M3U8DownLoadThread
import think.reed.refitshopmodule.mediacodec.MultiExtractorCodec
import think.reed.refitshopmodule.mediacodec.PlayThread
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by thinkreed on 2017/7/17.
 */
class WrapperActivity : AppCompatActivity() {

    private lateinit var playThread: PlayThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refitshop_fragment_wrapper)
        checkPermission()
//        var fragment = supportFragmentManager.findFragmentById(R.id.container)
//
//        if (fragment == null) {
//            fragment = RefitFragment.getInstance()
//            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
//        }
        text.text = stringFromJNI()
        EventBus.getDefault().register(this)
        startDecode()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe
    fun onBufferReadyEvent(bufferReadyEvent: BufferReadyEvent) {
        Log.d("thinkreed", "buffer ready")
        playThread.play()
    }

    private fun startDecode() {
        val musicList = CopyOnWriteArrayList<String>()
        val downloader = M3U8DownLoadThread(musicList)
        val multiDecoder = MultiExtractorCodec(musicList)
        val audio = multiDecoder.switcher.curComponent.audioTrack
        playThread = PlayThread(audio, multiDecoder)
        playThread.start()
        downloader.start()
        downloader.startDownloadSong()
    }

    private fun checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.CAMERA), 0)
            }
        }
    }

    external fun stringFromJNI(): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}