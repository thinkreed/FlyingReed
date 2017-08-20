package think.reed.refitshopmodule.controller.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import think.reed.refitshopmodule.R
import think.reed.refitshopmodule.controller.fragment.RefitFragment
import think.reed.refitshopmodule.mediacodec.TsExtractor

/**
 * Created by thinkreed on 2017/7/17.
 */
class WrapperActivity :AppCompatActivity(){

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
        TsExtractor().extractor("/sdcard/ts/playlist0.ts")
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