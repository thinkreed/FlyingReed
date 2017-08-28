package reed.flyingreed.common.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_empty.*
import reed.flyingreed.R
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.qihoo360.replugin.RePlugin


/**
 * Created by thinkreed on 2017/7/30.
 */

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_empty)
        checkPermission()
        message.text = "this is text activity"
        message.setOnClickListener {
            val intent = RePlugin.createIntent(
                    "player", "think.reed.tinyplayer.TinyPlayerEntryActivity")
            val result = RePlugin.startActivity(this@TestActivity, intent)
            Log.e("thinkreed", "start activity " + result)
        }
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