package think.reed.refitshopmodule.controller.activity

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import think.reed.refitshopmodule.R
import think.reed.refitshopmodule.mediacodec.M3U8DownLoadThread
import think.reed.refitshopmodule.mediacodec.MultiExtractorCodec
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by thinkreed on 2017/7/17.
 */
class WrapperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refitshop_activity_single_fragment)
        checkPermission()
//        var fragment = supportFragmentManager.findFragmentById(R.id.container)
//
//        if (fragment == null) {
//            fragment = RefitFragment.getInstance()
//            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
//        }

    }

    private fun startDecode() {
        val musicList = CopyOnWriteArrayList<String>()
        val downloader = M3U8DownLoadThread(musicList)
        val multiDecoder = MultiExtractorCodec(musicList)
        downloader.start()
        downloader.startDownloadSong()
        var audioMinBufSizeLocal = AudioTrack.getMinBufferSize(32000,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT)
        audioMinBufSizeLocal *= 2
        val audio = AudioTrack(AudioManager.STREAM_MUSIC, 32000, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, audioMinBufSizeLocal, AudioTrack.MODE_STREAM)
        audio.play()
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
}